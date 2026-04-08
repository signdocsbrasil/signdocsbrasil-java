package com.signdocsbrasil.api;

import com.signdocsbrasil.api.errors.BadRequestException;
import com.signdocsbrasil.api.errors.ServiceUnavailableException;
import com.signdocsbrasil.api.models.CreateTransactionRequest;
import com.signdocsbrasil.api.models.Policy;
import com.signdocsbrasil.api.models.Transaction;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the retry logic exercised through the full SDK stack.
 * Uses MockWebServer to simulate transient failures and verify retry behavior.
 */
class RetryIntegrationTest {

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

    private void enqueueToken() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));
    }

    private SignDocsBrasilClient createClient(int maxRetries) {
        return SignDocsBrasilClient.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .timeout(Duration.ofSeconds(5))
                .maxRetries(maxRetries)
                .build();
    }

    private static final String TX_RESPONSE = "{\"tenantId\":\"abc123\",\"transactionId\":\"tx-001\",\"status\":\"CREATED\"}";
    private static final String ERROR_503_BODY = "{\"type\":\"about:blank\",\"title\":\"Service Unavailable\",\"status\":503,\"detail\":\"Temporarily unavailable\"}";
    private static final String ERROR_429_BODY = "{\"type\":\"about:blank\",\"title\":\"Too Many Requests\",\"status\":429,\"detail\":\"Rate limited\"}";
    private static final String ERROR_400_BODY = "{\"type\":\"about:blank\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"Invalid input\"}";

    @Test
    void retries503ThenSucceeds() throws Exception {
        SignDocsBrasilClient client = createClient(3);
        enqueueToken();

        // First request: 503
        server.enqueue(new MockResponse()
                .setResponseCode(503)
                .setBody(ERROR_503_BODY)
                .setHeader("Content-Type", "application/json"));
        // Second request: 200
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TX_RESPONSE)
                .setHeader("Content-Type", "application/json"));

        Transaction tx = client.transactions().get("tx-001");

        assertNotNull(tx);
        assertEquals("tx-001", tx.getTransactionId());
        assertEquals("CREATED", tx.getStatus());

        // 1 token request + 2 API requests (503, then 200) = 3 total
        assertEquals(3, server.getRequestCount());
    }

    @Test
    void retries429WithRetryAfterThenSucceeds() throws Exception {
        SignDocsBrasilClient client = createClient(3);
        enqueueToken();

        // First request: 429 with Retry-After=1
        server.enqueue(new MockResponse()
                .setResponseCode(429)
                .setBody(ERROR_429_BODY)
                .setHeader("Content-Type", "application/json")
                .setHeader("Retry-After", "1"));
        // Second request: 200
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TX_RESPONSE)
                .setHeader("Content-Type", "application/json"));

        long startTime = System.currentTimeMillis();
        Transaction tx = client.transactions().get("tx-001");
        long elapsed = System.currentTimeMillis() - startTime;

        assertNotNull(tx);
        assertEquals("tx-001", tx.getTransactionId());

        // Retry-After=1 means 1 second delay; verify at least ~900ms passed
        assertTrue(elapsed >= 900, "Expected at least 900ms delay for Retry-After=1, got " + elapsed + "ms");

        // 1 token request + 2 API requests = 3 total
        assertEquals(3, server.getRequestCount());
    }

    @Test
    void retriesMaxRetries3WithFour503sThenSucceeds() throws Exception {
        SignDocsBrasilClient client = createClient(3);
        enqueueToken();

        // Enqueue 3 x 503 (will be retried)
        for (int i = 0; i < 3; i++) {
            server.enqueue(new MockResponse()
                    .setResponseCode(503)
                    .setBody(ERROR_503_BODY)
                    .setHeader("Content-Type", "application/json"));
        }
        // 4th attempt: 200 success
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TX_RESPONSE)
                .setHeader("Content-Type", "application/json"));

        Transaction tx = client.transactions().get("tx-001");

        assertNotNull(tx);
        assertEquals("tx-001", tx.getTransactionId());

        // 1 token + 4 API requests (3 retries + 1 success) = 5
        assertEquals(5, server.getRequestCount());
    }

    @Test
    void exhaustsRetriesAndThrowsServiceUnavailableException() {
        SignDocsBrasilClient client = createClient(3);
        enqueueToken();

        // Enqueue 4 x 503 (initial + 3 retries, all fail)
        for (int i = 0; i < 4; i++) {
            server.enqueue(new MockResponse()
                    .setResponseCode(503)
                    .setBody(ERROR_503_BODY)
                    .setHeader("Content-Type", "application/json"));
        }

        ServiceUnavailableException ex = assertThrows(ServiceUnavailableException.class,
                () -> client.transactions().get("tx-001"));

        assertEquals(503, ex.getStatus());

        // 1 token + 4 API requests (initial + 3 retries) = 5
        assertEquals(5, server.getRequestCount());
    }

    @Test
    void nonRetryable400DoesNotRetry() {
        SignDocsBrasilClient client = createClient(3);
        enqueueToken();

        // Single 400 - should NOT retry
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(ERROR_400_BODY)
                .setHeader("Content-Type", "application/json"));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> {
                    CreateTransactionRequest request = new CreateTransactionRequest();
                    request.setPurpose("DOCUMENT_SIGNATURE");
                    request.setPolicy(new Policy("INVALID"));
                    client.transactions().create(request);
                });

        assertEquals(400, ex.getStatus());

        // 1 token + 1 API request (no retry for 400) = 2
        assertEquals(2, server.getRequestCount());
    }
}
