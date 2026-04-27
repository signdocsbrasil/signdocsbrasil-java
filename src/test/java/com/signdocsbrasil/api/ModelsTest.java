package com.signdocsbrasil.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.signdocsbrasil.api.errors.ProblemDetail;
import com.signdocsbrasil.api.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for model deserialization from fixture JSON files.
 * Verifies that Gson can correctly parse the API response bodies into model objects.
 */
class ModelsTest {

    private static final Gson GSON = new GsonBuilder().create();
    private static Path fixturesDir;

    @BeforeAll
    static void findFixturesDir() {
        // Fixtures are at fixtures/ relative to the Java SDK project root
        fixturesDir = Paths.get(System.getProperty("user.dir"), "fixtures").normalize();
        assertTrue(Files.isDirectory(fixturesDir), "Fixtures directory not found: " + fixturesDir);
    }

    private static String readFixture(String name) throws IOException {
        Path filePath = fixturesDir.resolve(name);
        return Files.readString(filePath);
    }

    private static JsonObject readFixtureResponseBody(String name) throws IOException {
        String json = readFixture(name);
        JsonObject root = GSON.fromJson(json, JsonObject.class);
        return root.getAsJsonObject("response").getAsJsonObject("body");
    }

    @Test
    void transactionDeserializationFromCreateFixture() throws Exception {
        JsonObject body = readFixtureResponseBody("transactions-create.json");
        Transaction tx = GSON.fromJson(body, Transaction.class);

        assertEquals("abc123", tx.getTenantId());
        assertEquals("tx-uuid-001", tx.getTransactionId());
        assertEquals("CREATED", tx.getStatus());
        assertEquals("DOCUMENT_SIGNATURE", tx.getPurpose());
        assertNotNull(tx.getPolicy());
        assertEquals("CLICK_ONLY", tx.getPolicy().getProfile());
        assertNotNull(tx.getSigner());
        assertEquals("Jo\u00e3o Silva", tx.getSigner().getName());
        assertEquals("joao@example.com", tx.getSigner().getEmail());
        assertEquals("user-ext-001", tx.getSigner().getUserExternalId());
        assertEquals("12345678901", tx.getSigner().getCpf());
        assertNotNull(tx.getMetadata());
        assertEquals("CTR-2024-001", tx.getMetadata().get("contractId"));
        assertNotNull(tx.getSteps());
        assertEquals(1, tx.getSteps().size());
        assertEquals("step-uuid-001", tx.getSteps().get(0).getStepId());
        assertEquals("CLICK_ACCEPT", tx.getSteps().get(0).getType());
        assertEquals("PENDING", tx.getSteps().get(0).getStatus());
        assertEquals(1, tx.getSteps().get(0).getOrder());
        assertEquals(0, tx.getSteps().get(0).getAttempts());
        assertEquals(3, tx.getSteps().get(0).getMaxAttempts());
        assertEquals("2024-11-16T00:00:00.000Z", tx.getExpiresAt());
        assertEquals("2024-11-15T00:00:00.000Z", tx.getCreatedAt());
        assertEquals("2024-11-15T00:00:00.000Z", tx.getUpdatedAt());
    }

    @Test
    void transactionWithStepsFromGetFixture() throws Exception {
        JsonObject body = readFixtureResponseBody("transactions-get.json");
        Transaction tx = GSON.fromJson(body, Transaction.class);

        assertEquals("tx-uuid-001", tx.getTransactionId());
        assertEquals("IN_PROGRESS", tx.getStatus());
        assertEquals("CLICK_PLUS_OTP", tx.getPolicy().getProfile());
        assertNotNull(tx.getSteps());
        assertEquals(2, tx.getSteps().size());

        // First step: COMPLETED with click result
        Step step1 = tx.getSteps().get(0);
        assertEquals("step-uuid-001", step1.getStepId());
        assertEquals("CLICK_ACCEPT", step1.getType());
        assertEquals("COMPLETED", step1.getStatus());
        assertEquals(1, step1.getOrder());
        assertEquals(1, step1.getAttempts());
        assertEquals(3, step1.getMaxAttempts());
        assertEquals("2024-11-15T00:01:00.000Z", step1.getCompletedAt());

        // Verify nested StepResult.Click
        assertNotNull(step1.getResult());
        assertNotNull(step1.getResult().getClick());
        assertTrue(step1.getResult().getClick().isAccepted());
        assertEquals("v1.0", step1.getResult().getClick().getTextVersion());

        // Second step: PENDING OTP
        Step step2 = tx.getSteps().get(1);
        assertEquals("step-uuid-002", step2.getStepId());
        assertEquals("OTP_CHALLENGE", step2.getType());
        assertEquals("PENDING", step2.getStatus());
        assertEquals(2, step2.getOrder());
        assertEquals(0, step2.getAttempts());
        assertNull(step2.getResult());
    }

    @Test
    void transactionListResponseFromListFixture() throws Exception {
        JsonObject body = readFixtureResponseBody("transactions-list.json");
        TransactionListResponse response = GSON.fromJson(body, TransactionListResponse.class);

        assertEquals(2, response.getCount());
        assertNotNull(response.getNextToken());
        assertEquals("eyJQSyI6IlRFTkFOVCNhYmMxMjMiLCJTSyI6IlRYI3R4LXV1aWQtMDAzIn0=", response.getNextToken());
        assertNotNull(response.getTransactions());
        assertEquals(2, response.getTransactions().size());

        Transaction tx1 = response.getTransactions().get(0);
        assertEquals("tx-uuid-002", tx1.getTransactionId());
        assertEquals("COMPLETED", tx1.getStatus());
        assertEquals("Maria Santos", tx1.getSigner().getName());
        assertEquals("CLICK_ONLY", tx1.getPolicy().getProfile());

        Transaction tx2 = response.getTransactions().get(1);
        assertEquals("tx-uuid-003", tx2.getTransactionId());
        assertEquals("COMPLETED", tx2.getStatus());
        assertEquals("Pedro Costa", tx2.getSigner().getName());
        assertEquals("BIOMETRIC", tx2.getPolicy().getProfile());
    }

    @Test
    void evidenceFromGetFixture() throws Exception {
        JsonObject body = readFixtureResponseBody("evidence-get.json");
        Evidence evidence = GSON.fromJson(body, Evidence.class);

        assertEquals("abc123", evidence.getTenantId());
        assertEquals("tx-uuid-001", evidence.getTransactionId());
        assertEquals("ev-uuid-001", evidence.getEvidenceId());
        assertEquals("COMPLETED", evidence.getStatus());
        assertEquals("2024-11-15T00:00:00.000Z", evidence.getCreatedAt());
        assertEquals("2024-11-15T00:01:00.000Z", evidence.getCompletedAt());

        // Verify nested signer
        assertNotNull(evidence.getSigner());
        assertEquals("Jo\u00e3o Silva", evidence.getSigner().getName());
        assertEquals("12345678901", evidence.getSigner().getCpf());
        assertEquals("user-ext-001", evidence.getSigner().getUserExternalId());

        // Verify nested steps
        assertNotNull(evidence.getSteps());
        assertEquals(1, evidence.getSteps().size());
        Evidence.EvidenceStep step = evidence.getSteps().get(0);
        assertEquals("CLICK_ACCEPT", step.getType());
        assertEquals("COMPLETED", step.getStatus());
        assertEquals("2024-11-15T00:01:00.000Z", step.getCompletedAt());
        assertNotNull(step.getResult());

        // Verify nested document
        assertNotNull(evidence.getDocument());
        assertEquals("a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2", evidence.getDocument().getHash());
        assertEquals("contract.pdf", evidence.getDocument().getFilename());
    }

    @Test
    void problemDetailFromError400Fixture() throws Exception {
        String json = readFixture("error-400.json");
        JsonObject root = GSON.fromJson(json, JsonObject.class);
        JsonObject body = root.getAsJsonObject("response").getAsJsonObject("body");
        ProblemDetail pd = GSON.fromJson(body, ProblemDetail.class);

        assertEquals("https://api.signdocs.com.br/errors/bad-request", pd.getType());
        assertEquals("Bad Request", pd.getTitle());
        assertEquals(400, pd.getStatus());
        assertEquals("Invalid policy profile: UNKNOWN_PROFILE", pd.getDetail());
        assertEquals("/v1/transactions", pd.getInstance());
    }

    @Test
    void webhookTestResponseDeserializesNestedTestDelivery() {
        // Mirror the live API response shape (per OpenAPI spec for
        // POST /v1/webhooks/{webhookId}/test): top-level webhookId + nested
        // testDelivery {httpStatus, success, error?, timestamp}.
        String json = "{"
                + "\"webhookId\":\"wh_abc123\","
                + "\"testDelivery\":{"
                + "\"httpStatus\":200,"
                + "\"success\":true,"
                + "\"timestamp\":\"2026-04-27T01:23:28.323Z\""
                + "}}";

        WebhookTestResponse resp = GSON.fromJson(json, WebhookTestResponse.class);

        assertEquals("wh_abc123", resp.getWebhookId());
        assertNotNull(resp.getTestDelivery());
        assertEquals(200, resp.getTestDelivery().getHttpStatus());
        assertTrue(resp.getTestDelivery().isSuccess());
        assertNull(resp.getTestDelivery().getError());
        assertEquals("2026-04-27T01:23:28.323Z", resp.getTestDelivery().getTimestamp());
    }

    @Test
    void webhookTestResponseDeserializesFailedDeliveryWithError() {
        String json = "{"
                + "\"webhookId\":\"wh_abc123\","
                + "\"testDelivery\":{"
                + "\"httpStatus\":500,"
                + "\"success\":false,"
                + "\"error\":\"Internal Server Error\","
                + "\"timestamp\":\"2026-04-27T01:23:28.323Z\""
                + "}}";

        WebhookTestResponse resp = GSON.fromJson(json, WebhookTestResponse.class);

        assertEquals("wh_abc123", resp.getWebhookId());
        assertEquals(500, resp.getTestDelivery().getHttpStatus());
        assertFalse(resp.getTestDelivery().isSuccess());
        assertEquals("Internal Server Error", resp.getTestDelivery().getError());
    }

    @Test
    void registerWebhookResponseFromFixture() throws Exception {
        JsonObject body = readFixtureResponseBody("webhooks-register.json");
        RegisterWebhookResponse response = GSON.fromJson(body, RegisterWebhookResponse.class);

        assertEquals("wh-uuid-001", response.getWebhookId());
        assertEquals("https://example.com/webhooks/signdocs", response.getUrl());
        assertEquals("whsec_generated_secret_abc123", response.getSecret());
        assertNotNull(response.getEvents());
        assertEquals(2, response.getEvents().size());
        assertEquals("TRANSACTION.COMPLETED", response.getEvents().get(0));
        assertEquals("TRANSACTION.FAILED", response.getEvents().get(1));
        assertEquals("ACTIVE", response.getStatus());
        assertEquals("2024-11-15T00:00:00.000Z", response.getCreatedAt());
    }
}
