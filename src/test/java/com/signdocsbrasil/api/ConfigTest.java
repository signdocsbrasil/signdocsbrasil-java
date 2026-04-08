package com.signdocsbrasil.api;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void builderDefaults() {
        Config config = Config.builder()
                .clientId("test")
                .clientSecret("secret")
                .build();

        assertEquals("test", config.getClientId());
        assertEquals("secret", config.getClientSecret());
        assertEquals(Config.DEFAULT_BASE_URL, config.getBaseUrl());
        assertEquals(Config.DEFAULT_TIMEOUT, config.getTimeout());
        assertEquals(Config.DEFAULT_MAX_RETRIES, config.getMaxRetries());
        assertEquals(Config.DEFAULT_SCOPES, config.getScopes());
    }

    @Test
    void builderCustomValues() {
        Config config = Config.builder()
                .clientId("my-client")
                .clientSecret("my-secret")
                .baseUrl("https://custom.api.com")
                .timeout(Duration.ofSeconds(5))
                .maxRetries(2)
                .scopes(Arrays.asList("custom:scope"))
                .build();

        assertEquals("https://custom.api.com", config.getBaseUrl());
        assertEquals(Duration.ofSeconds(5), config.getTimeout());
        assertEquals(2, config.getMaxRetries());
        assertEquals(1, config.getScopes().size());
        assertEquals("custom:scope", config.getScopes().get(0));
    }

    @Test
    void missingClientIdThrows() {
        assertThrows(NullPointerException.class, () ->
                Config.builder().clientId(null));
    }

    @Test
    void emptyClientIdThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                Config.builder().clientId("").clientSecret("secret").build());
    }

    @Test
    void noAuthThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                Config.builder().clientId("test").build());
    }

    @Test
    void privateKeyWithoutKidThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                Config.builder().clientId("test").privateKey("pem-data").build());
    }

    @Test
    void usesClientSecret() {
        Config config = Config.builder().clientId("test").clientSecret("secret").build();
        assertTrue(config.usesClientSecret());
        assertFalse(config.usesPrivateKeyJwt());
    }

    @Test
    void usesPrivateKeyJwt() {
        Config config = Config.builder().clientId("test").privateKey("pem-data").kid("kid-1").build();
        assertFalse(config.usesClientSecret());
        assertTrue(config.usesPrivateKeyJwt());
    }

    @Test
    void tokenUrlConstructed() {
        Config config = Config.builder().clientId("test").clientSecret("secret")
                .baseUrl("https://custom.api.com").build();
        assertEquals("https://custom.api.com/oauth2/token", config.getTokenUrl());
    }
}
