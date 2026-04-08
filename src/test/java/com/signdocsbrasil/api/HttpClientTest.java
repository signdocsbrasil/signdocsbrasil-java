package com.signdocsbrasil.api;

import com.google.gson.reflect.TypeToken;
import com.signdocsbrasil.api.errors.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

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

    private HttpClient createHttpClient() {
        // Enqueue token response for auth
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"test-token\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));

        String baseUrl = server.url("").toString().replaceAll("/$", "");
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl)
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        return new HttpClient(config, auth);
    }

    @Test
    void authorizationHeader() throws Exception {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json"));

        client.request("GET", "/v1/test", null,
                new TypeToken<Map<String, Object>>() {}.getType());

        // Skip token request
        server.takeRequest();
        RecordedRequest apiReq = server.takeRequest();
        assertTrue(apiReq.getHeader("Authorization").startsWith("Bearer "));
    }

    @Test
    void userAgentHeader() throws Exception {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse()
                .setBody("{\"ok\":true}")
                .setHeader("Content-Type", "application/json"));

        client.request("GET", "/v1/test", null,
                new TypeToken<Map<String, Object>>() {}.getType());

        server.takeRequest();
        RecordedRequest apiReq = server.takeRequest();
        assertTrue(apiReq.getHeader("User-Agent").contains("signdocs-brasil-java/"));
    }

    @Test
    void noAuthSkipsAuthorization() throws Exception {
        // No token request needed for noAuth
        String baseUrl = server.url("").toString().replaceAll("/$", "");
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl)
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient client = new HttpClient(config, auth);

        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\"}")
                .setHeader("Content-Type", "application/json"));

        client.requestNoAuth("GET", "/health",
                new TypeToken<Map<String, Object>>() {}.getType());

        RecordedRequest req = server.takeRequest();
        assertNull(req.getHeader("Authorization"));
    }

    @Test
    void jsonBody() throws Exception {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse()
                .setBody("{\"id\":\"tx_1\"}")
                .setHeader("Content-Type", "application/json"));

        Map<String, String> body = Map.of("name", "test");
        client.request("POST", "/v1/transactions", body,
                new TypeToken<Map<String, Object>>() {}.getType());

        server.takeRequest();
        RecordedRequest apiReq = server.takeRequest();
        assertTrue(apiReq.getHeader("Content-Type").contains("application/json"));
        assertTrue(apiReq.getBody().readUtf8().contains("\"name\""));
    }

    @Test
    void returns204AsNull() {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse().setResponseCode(204));

        Object result = client.request("DELETE", "/v1/webhooks/123", null,
                new TypeToken<Map<String, Object>>() {}.getType());

        assertNull(result);
    }

    @Test
    void throws400BadRequest() {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"type\":\"about:blank\",\"title\":\"Bad Request\",\"status\":400}")
                .setHeader("Content-Type", "application/json"));

        assertThrows(BadRequestException.class, () ->
                client.request("POST", "/v1/test", null,
                        new TypeToken<Map<String, Object>>() {}.getType()));
    }

    @Test
    void throws404NotFound() {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"type\":\"about:blank\",\"title\":\"Not Found\",\"status\":404}")
                .setHeader("Content-Type", "application/json"));

        assertThrows(NotFoundException.class, () ->
                client.request("GET", "/v1/transactions/missing", null,
                        new TypeToken<Map<String, Object>>() {}.getType()));
    }

    @Test
    void throws429WithRetryAfter() {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse()
                .setResponseCode(429)
                .setBody("{\"type\":\"about:blank\",\"title\":\"Rate Limited\",\"status\":429}")
                .setHeader("Content-Type", "application/json")
                .setHeader("Retry-After", "5"));

        try {
            client.request("GET", "/v1/test", null,
                    new TypeToken<Map<String, Object>>() {}.getType());
            fail("Should have thrown");
        } catch (RateLimitException e) {
            assertEquals(429, e.getStatus());
            assertEquals(5, e.getRetryAfterSeconds());
        }
    }

    @Test
    void idempotencyKeyExplicit() throws Exception {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse()
                .setBody("{\"id\":\"tx_1\"}")
                .setHeader("Content-Type", "application/json"));

        client.requestWithIdempotency("POST", "/v1/transactions",
                Map.of("name", "test"),
                new TypeToken<Map<String, Object>>() {}.getType(),
                "custom-key");

        server.takeRequest();
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("custom-key", apiReq.getHeader("X-Idempotency-Key"));
    }

    @Test
    void idempotencyKeyAutoGenerated() throws Exception {
        HttpClient client = createHttpClient();
        server.enqueue(new MockResponse()
                .setBody("{\"id\":\"tx_1\"}")
                .setHeader("Content-Type", "application/json"));

        client.requestWithIdempotency("POST", "/v1/transactions",
                Map.of("name", "test"),
                new TypeToken<Map<String, Object>>() {}.getType(),
                null);

        server.takeRequest();
        RecordedRequest apiReq = server.takeRequest();
        assertNotNull(apiReq.getHeader("X-Idempotency-Key"));
        assertFalse(apiReq.getHeader("X-Idempotency-Key").isEmpty());
    }
}
