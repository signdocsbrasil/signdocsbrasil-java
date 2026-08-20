package com.signdocsbrasil.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.signdocsbrasil.api.errors.ConflictException;
import com.signdocsbrasil.api.models.MintSigningLinkResponse;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Minting a signing link re-issues a single-use URL for an ACTIVE session
 * without creating another transaction and without consuming quota.
 */
class SigningLinkTest {

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

    private SignDocsBrasilClient createClient() {
        return SignDocsBrasilClient.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(server.url("").toString().replaceAll("/$", ""))
                .maxRetries(0)
                .build();
    }

    private void enqueueToken() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"t\",\"token_type\":\"Bearer\",\"expires_in\":900}")
                .setHeader("Content-Type", "application/json"));
    }

    @Test
    void mintsALinkWithoutAnIdempotencyKey() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"ss_1\",\"transactionId\":\"tx_1\","
                        + "\"url\":\"https://sign.signdocs.com.br/s/ss_1?cs=abc\","
                        + "\"expiresAt\":\"2026-08-27T12:00:00.000Z\",\"expiresIn\":3600}")
                .setHeader("Content-Type", "application/json"));

        MintSigningLinkResponse link = createClient().signingSessions().link("ss_1");

        assertEquals("https://sign.signdocs.com.br/s/ss_1?cs=abc", link.getUrl());
        assertEquals(3600, link.getExpiresIn());
        assertEquals("tx_1", link.getTransactionId());
        assertEquals("2026-08-27T12:00:00.000Z", link.getExpiresAt());

        server.takeRequest(); // token
        RecordedRequest req = server.takeRequest();
        assertEquals("POST", req.getMethod());
        assertEquals("/v1/signing-sessions/ss_1/link", req.getPath());
        // Not a metered create: a key would let a retry replay a URL that has
        // already been consumed instead of issuing the fresh one asked for.
        assertNull(req.getHeader("X-Idempotency-Key"));
    }

    @Test
    void surfacesConflictWhenTheSessionIsNotActive() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(409)
                .setBody("{\"type\":\"about:blank\",\"title\":\"Conflict\",\"status\":409,"
                        + "\"detail\":\"Session cannot be linked in status: COMPLETED\"}")
                .setHeader("Content-Type", "application/problem+json"));

        SignDocsBrasilClient client = createClient();

        // A link to a finished session would authenticate nothing.
        assertThrows(ConflictException.class, () -> client.signingSessions().link("ss_done"));
    }
}
