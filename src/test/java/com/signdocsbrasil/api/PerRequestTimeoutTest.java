package com.signdocsbrasil.api;

import com.google.gson.reflect.TypeToken;
import com.signdocsbrasil.api.errors.TimeoutException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that per-request timeout overrides the default client timeout.
 */
class PerRequestTimeoutTest {

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

    @Test
    void perRequestTimeoutOverridesDefault() {
        // Default timeout is 30 seconds, but we set a per-request timeout of 500ms
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .timeout(Duration.ofSeconds(30))
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient client = new HttpClient(config, auth);

        // Server accepts connection but never sends a response — guarantees timeout
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // The per-request timeout of 500ms should trigger before the 30s default
        assertThrows(TimeoutException.class, () ->
                client.requestNoAuth("GET", "/health",
                        new TypeToken<Map<String, Object>>() {}.getType(),
                        Duration.ofMillis(500)));
    }

    @Test
    void requestWithoutPerRequestTimeoutUsesDefault() {
        // Default timeout is 30 seconds
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .timeout(Duration.ofSeconds(30))
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient client = new HttpClient(config, auth);

        // Enqueue an immediate response -- should succeed with 30s default timeout
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\"}")
                .setHeader("Content-Type", "application/json"));

        var result = client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        assertNotNull(result);
    }

    @Test
    void perRequestTimeoutOnAuthenticatedRequest() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .timeout(Duration.ofSeconds(30))
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient client = new HttpClient(config, auth);

        // Enqueue token response (fast)
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        // Server accepts connection but never sends a response for the API call
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE));

        // Per-request timeout of 500ms on an authenticated request
        assertThrows(TimeoutException.class, () ->
                client.request("GET", "/v1/transactions/tx_1", null,
                        new TypeToken<Map<String, Object>>() {}.getType(),
                        Duration.ofMillis(500)));
    }

    @Test
    void shortDefaultTimeoutOverriddenByLongerPerRequest() {
        // Default timeout is 200ms (very short)
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .timeout(Duration.ofMillis(200))
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient client = new HttpClient(config, auth);

        // Enqueue a response with a 500ms delay
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\"}")
                .setHeader("Content-Type", "application/json")
                .setBodyDelay(500, TimeUnit.MILLISECONDS));

        // Use a per-request timeout of 5 seconds, which should allow the 500ms delay
        var result = client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType(),
                Duration.ofSeconds(5));

        assertNotNull(result);
    }

    @Test
    void nullPerRequestTimeoutUsesDefault() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .timeout(Duration.ofSeconds(30))
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient client = new HttpClient(config, auth);

        // Enqueue an immediate response
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\"}")
                .setHeader("Content-Type", "application/json"));

        // Passing null explicitly should use the default timeout
        var result = client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType(),
                null);

        assertNotNull(result);
    }
}
