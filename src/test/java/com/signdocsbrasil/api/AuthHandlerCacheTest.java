package com.signdocsbrasil.api;

import com.signdocsbrasil.api.tokencache.CachedToken;
import com.signdocsbrasil.api.tokencache.InMemoryTokenCache;
import com.signdocsbrasil.api.tokencache.TokenCache;
import com.signdocsbrasil.api.tokencache.TokenCacheKeys;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthHandlerCacheTest {

    private MockWebServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

    private String baseUrl() {
        return server.url("").toString().replaceAll("/$", "");
    }

    private Config.Builder baseConfig(TokenCache cache) {
        return Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .tokenCache(cache);
    }

    @Test
    void cacheHitSkipsTokenEndpoint() {
        TokenCache cache = new InMemoryTokenCache();
        String key = TokenCacheKeys.derive(
                "test-client", baseUrl(), Config.DEFAULT_SCOPES);
        cache.set(key, new CachedToken(
                "preloaded-token",
                Instant.now().plusSeconds(3600)));

        AuthHandler auth = new AuthHandler(baseConfig(cache).build());
        assertEquals("preloaded-token", auth.getAccessToken());
        assertEquals(0, server.getRequestCount(), "token endpoint should not be hit on cache hit");
    }

    @Test
    void cacheMissFetchesAndStores() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"fresh-token\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        TokenCache cache = new InMemoryTokenCache();
        AuthHandler auth = new AuthHandler(baseConfig(cache).build());

        assertEquals("fresh-token", auth.getAccessToken());

        String key = TokenCacheKeys.derive(
                "test-client", baseUrl(), Config.DEFAULT_SCOPES);
        Optional<CachedToken> stored = cache.get(key);
        assertTrue(stored.isPresent(), "cache should contain the fetched token");
        assertEquals("fresh-token", stored.get().getAccessToken());
    }

    @Test
    void cacheSharedAcrossInstances() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"shared-token\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        TokenCache cache = new InMemoryTokenCache();
        AuthHandler first = new AuthHandler(baseConfig(cache).build());
        AuthHandler second = new AuthHandler(baseConfig(cache).build());

        assertEquals("shared-token", first.getAccessToken());
        assertEquals("shared-token", second.getAccessToken());
        assertEquals(1, server.getRequestCount(), "second handler should reuse cached token");
    }

    @Test
    void expiredCacheTriggersRefresh() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"refreshed-token\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        TokenCache cache = new InMemoryTokenCache();
        String key = TokenCacheKeys.derive(
                "test-client", baseUrl(), Config.DEFAULT_SCOPES);
        // A token 10s from expiry is inside the 30s skew window.
        cache.set(key, new CachedToken(
                "stale-token",
                Instant.now().plusSeconds(10)));

        AuthHandler auth = new AuthHandler(baseConfig(cache).build());
        assertEquals("refreshed-token", auth.getAccessToken());
        assertEquals(1, server.getRequestCount());
    }

    @Test
    void invalidateDropsCachedToken() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok-1\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok-2\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        TokenCache cache = new InMemoryTokenCache();
        AuthHandler auth = new AuthHandler(baseConfig(cache).build());

        assertEquals("tok-1", auth.getAccessToken());
        auth.invalidate();
        assertEquals("tok-2", auth.getAccessToken());
        assertEquals(2, server.getRequestCount());
    }

    // ---- TokenCacheKeys ---------------------------------------------

    @Test
    void keyIsDeterministic() {
        List<String> scopes = Arrays.asList("a:read", "b:write");
        String k1 = TokenCacheKeys.derive("id", "https://api.example", scopes);
        String k2 = TokenCacheKeys.derive("id", "https://api.example", scopes);
        assertEquals(k1, k2);
    }

    @Test
    void keyIndependentOfScopeOrdering() {
        String k1 = TokenCacheKeys.derive("id", "https://api.example",
                Arrays.asList("a", "b", "c"));
        String k2 = TokenCacheKeys.derive("id", "https://api.example",
                Arrays.asList("c", "b", "a"));
        assertEquals(k1, k2, "scopes should be canonicalized before hashing");
    }

    @Test
    void keyIndependentOfTrailingSlash() {
        String k1 = TokenCacheKeys.derive("id", "https://api.example",
                Collections.emptyList());
        String k2 = TokenCacheKeys.derive("id", "https://api.example/",
                Collections.emptyList());
        String k3 = TokenCacheKeys.derive("id", "https://api.example///",
                Collections.emptyList());
        assertEquals(k1, k2);
        assertEquals(k1, k3);
    }

    @Test
    void keyHasPrefixAndLength() {
        String key = TokenCacheKeys.derive("id", "https://api.example",
                Collections.emptyList());
        assertTrue(key.startsWith("signdocs.oauth."),
                "expected prefix 'signdocs.oauth.' but got: " + key);
        // prefix + 32 hex chars
        assertEquals("signdocs.oauth.".length() + 32, key.length());
        String suffix = key.substring("signdocs.oauth.".length());
        assertTrue(suffix.matches("[0-9a-f]{32}"),
                "suffix should be 32 lower-case hex chars: " + suffix);
    }

    @Test
    void keyDoesNotLeakClientId() {
        String clientId = "leak-me-please-12345";
        String key = TokenCacheKeys.derive(clientId, "https://api.example",
                Collections.emptyList());
        assertFalse(key.contains(clientId),
                "cache key should not embed the client ID");
    }

    @Test
    void differentCredentialsYieldDifferentKeys() {
        String a = TokenCacheKeys.derive("client-A", "https://api.example",
                Collections.emptyList());
        String b = TokenCacheKeys.derive("client-B", "https://api.example",
                Collections.emptyList());
        assertNotEquals(a, b);
    }

    // ---- non-final invariant -----------------------------------------

    @Test
    void authHandlerClassIsNotFinal() {
        assertFalse(
                Modifier.isFinal(AuthHandler.class.getModifiers()),
                "AuthHandler must not be final so consumers can subclass it for custom flows");
    }

    // ---- CachedToken expiry ------------------------------------------

    @Test
    void cachedTokenExpiryRespectsSkew() {
        Instant now = Instant.now();
        CachedToken token = new CachedToken("t", now.plusSeconds(20));
        // 30-second skew means a token expiring in 20s is already "expired".
        assertTrue(token.isExpired(now, Duration.ofSeconds(30)));
        // Zero skew means it's still valid.
        assertFalse(token.isExpired(now, Duration.ZERO));
    }
}
