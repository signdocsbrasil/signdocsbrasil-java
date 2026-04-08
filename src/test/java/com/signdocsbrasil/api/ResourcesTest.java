package com.signdocsbrasil.api;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ResourcesTest {

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

    private void enqueueToken() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));
    }

    private String baseUrl() {
        return server.url("").toString().replaceAll("/$", "");
    }

    private HttpClient createHttpClient() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        return new HttpClient(config, auth);
    }

    @Test
    void healthCheckNoAuth() throws Exception {
        // No token request needed for noAuth
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\",\"version\":\"1.0.0\"}")
                .setHeader("Content-Type", "application/json"));

        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient httpClient = new HttpClient(config, auth);

        var result = httpClient.requestNoAuth("GET", "/health",
                new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>() {}.getType());

        RecordedRequest req = server.takeRequest();
        assertNull(req.getHeader("Authorization"));
        assertEquals("GET", req.getMethod());
        assertTrue(req.getPath().contains("/health"));
    }

    @Test
    void webhookDeleteReturns204() {
        enqueueToken();
        server.enqueue(new MockResponse().setResponseCode(204));

        HttpClient httpClient = createHttpClient();
        Object result = httpClient.request("DELETE", "/v1/webhooks/wh_1", null,
                new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>() {}.getType());

        assertNull(result);
    }

    @Test
    void webhookRegisterReturns201() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody("{\"id\":\"wh_1\",\"secret\":\"whsec_123\"}")
                .setHeader("Content-Type", "application/json"));

        HttpClient httpClient = createHttpClient();
        @SuppressWarnings("unchecked")
        var result = (java.util.Map<String, Object>) httpClient.request(
                "POST", "/v1/webhooks",
                java.util.Map.of("url", "https://example.com/hook"),
                new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>() {}.getType());

        assertNotNull(result);
        assertEquals("wh_1", result.get("id"));
    }

    @Test
    void userEnrollUsePUT() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"enrolled\":true}")
                .setHeader("Content-Type", "application/json"));

        HttpClient httpClient = createHttpClient();
        httpClient.request("PUT", "/v1/users/ext_1/enrollment",
                java.util.Map.of("name", "Test User"),
                new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>() {}.getType());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("PUT", apiReq.getMethod());
        assertTrue(apiReq.getPath().contains("/users/ext_1/enrollment"));
    }

    @Test
    void verificationNoAuth() throws Exception {
        server.enqueue(new MockResponse()
                .setBody("{\"valid\":true}")
                .setHeader("Content-Type", "application/json"));

        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient httpClient = new HttpClient(config, auth);

        httpClient.requestNoAuth("GET", "/v1/verify/ev_1",
                new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>() {}.getType());

        RecordedRequest req = server.takeRequest();
        assertNull(req.getHeader("Authorization"));
    }

    @Test
    void transactionCancelDeleteReturns200WithBody() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"tx_1\",\"status\":\"CANCELLED\"}")
                .setHeader("Content-Type", "application/json"));

        HttpClient httpClient = createHttpClient();
        @SuppressWarnings("unchecked")
        var result = (java.util.Map<String, Object>) httpClient.request(
                "DELETE", "/v1/transactions/tx_1", null,
                new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>() {}.getType());

        assertNotNull(result);
        assertEquals("CANCELLED", result.get("status"));

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("DELETE", apiReq.getMethod());
    }

    @Test
    void documentUploadPost() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"id\":\"tx_1\"}")
                .setHeader("Content-Type", "application/json"));

        HttpClient httpClient = createHttpClient();
        httpClient.request("POST", "/v1/transactions/tx_1/document",
                java.util.Map.of("content", "base64data"),
                new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>() {}.getType());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertTrue(apiReq.getPath().contains("/document"));
    }

    @Test
    void queryParameters() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[]}")
                .setHeader("Content-Type", "application/json"));

        HttpClient httpClient = createHttpClient();
        httpClient.requestWithQuery("GET", "/v1/transactions",
                new com.google.gson.reflect.TypeToken<java.util.Map<String, Object>>() {}.getType(),
                java.util.Map.of("status", "COMPLETED", "limit", "10"));

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertTrue(apiReq.getPath().contains("status=COMPLETED"));
        assertTrue(apiReq.getPath().contains("limit=10"));
    }
}
