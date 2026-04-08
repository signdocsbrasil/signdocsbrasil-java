package com.signdocsbrasil.api;

import com.google.gson.reflect.TypeToken;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that a custom {@link java.net.http.HttpClient} provided via Config
 * is used for API requests instead of the internally created one.
 */
class CustomHttpClientTest {

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
    void customHttpClientIsUsedForRequests() throws Exception {
        // Build a custom java.net.http.HttpClient with a specific connect timeout
        java.net.http.HttpClient customClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .httpClient(customClient)
                .build();

        // Verify the config stores the custom client
        assertSame(customClient, config.getHttpClient());

        AuthHandler auth = new AuthHandler(config);
        HttpClient httpClient = new HttpClient(config, auth);

        // Enqueue a health response (noAuth, so no token request needed)
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\"}")
                .setHeader("Content-Type", "application/json"));

        httpClient.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        // Verify the request was received (proving the custom client is functional)
        RecordedRequest req = server.takeRequest();
        assertEquals("GET", req.getMethod());
        assertTrue(req.getPath().contains("/health"));
    }

    @Test
    void defaultHttpClientWhenNoneProvided() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .build();

        // When no custom client is provided, getHttpClient returns null
        assertNull(config.getHttpClient());
    }

    @Test
    void customHttpClientViaSignDocsBrasilClientBuilder() throws Exception {
        java.net.http.HttpClient customClient = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        // Enqueue token + health responses
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\",\"version\":\"1.0.0\"}")
                .setHeader("Content-Type", "application/json"));

        // Build via the top-level client builder
        SignDocsBrasilClient client = SignDocsBrasilClient.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .httpClient(customClient)
                .build();

        // The health check should work using the custom client
        var result = client.health().check();
        assertNotNull(result);
    }
}
