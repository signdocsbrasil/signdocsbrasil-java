package com.signdocsbrasil.api;

import com.signdocsbrasil.api.errors.*;
import com.signdocsbrasil.api.resources.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 3: Resource error path tests.
 * Validates that each resource correctly propagates typed exceptions
 * for all documented error status codes.
 */
class ErrorPathsTest {

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

    private MockResponse problemResponse(int status, String title, String detail) {
        String body = String.format(
                "{\"type\":\"https://api.signdocs.com.br/errors/%s\",\"title\":\"%s\",\"status\":%d,\"detail\":\"%s\"}",
                title.toLowerCase().replace(" ", "-"), title, status, detail
        );
        return new MockResponse()
                .setResponseCode(status)
                .setBody(body)
                .setHeader("Content-Type", "application/problem+json");
    }

    // ── Transactions ──────────────────────────────────────────

    @Test
    void transactionCreate400InvalidPolicy() {
        enqueueToken();
        server.enqueue(problemResponse(400, "Bad Request", "Invalid policy profile: UNKNOWN_PROFILE"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                tx.create(new com.signdocsbrasil.api.models.CreateTransactionRequest()));
        assertEquals(400, ex.getStatus());
        assertTrue(ex.getProblemDetail().getDetail().contains("Invalid policy profile"));
    }

    @Test
    void transactionGet404Nonexistent() {
        enqueueToken();
        server.enqueue(problemResponse(404, "Not Found", "Transaction tx-nonexistent not found"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                tx.get("tx-nonexistent"));
        assertEquals(404, ex.getStatus());
        assertTrue(ex.getProblemDetail().getDetail().contains("not found"));
    }

    @Test
    void transactionCreate409Conflict() {
        enqueueToken();
        server.enqueue(problemResponse(409, "Conflict", "Transaction tx-uuid-001 is already finalized"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        ConflictException ex = assertThrows(ConflictException.class, () ->
                tx.create(new com.signdocsbrasil.api.models.CreateTransactionRequest()));
        assertEquals(409, ex.getStatus());
        assertTrue(ex.getProblemDetail().getDetail().contains("already finalized"));
    }

    @Test
    void transactionFinalize409AlreadyFinalized() {
        enqueueToken();
        server.enqueue(problemResponse(409, "Conflict", "Transaction tx_1 is already finalized"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        ConflictException ex = assertThrows(ConflictException.class, () ->
                tx.finalize("tx_1"));
        assertEquals(409, ex.getStatus());
    }

    @Test
    void transactionCancel400WrongState() {
        enqueueToken();
        server.enqueue(problemResponse(400, "Bad Request", "Transaction cannot be cancelled in current state"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                tx.cancel("tx_1"));
        assertEquals(400, ex.getStatus());
    }

    @Test
    void transactionList403InsufficientScope() {
        enqueueToken();
        server.enqueue(problemResponse(403, "Forbidden", "Missing required scope: transactions:read"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        ForbiddenException ex = assertThrows(ForbiddenException.class, () ->
                tx.list(null));
        assertEquals(403, ex.getStatus());
        assertTrue(ex.getProblemDetail().getDetail().contains("transactions:read"));
    }

    @Test
    void transactionList429RateLimit() {
        enqueueToken();
        server.enqueue(problemResponse(429, "Too Many Requests", "Rate limit exceeded")
                .setHeader("Retry-After", "10"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        RateLimitException ex = assertThrows(RateLimitException.class, () ->
                tx.list(null));
        assertEquals(429, ex.getStatus());
    }

    @Test
    void transactionGet500InternalError() {
        enqueueToken();
        server.enqueue(problemResponse(500, "Internal Server Error", "Unexpected error"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        InternalServerException ex = assertThrows(InternalServerException.class, () ->
                tx.get("tx_1"));
        assertEquals(500, ex.getStatus());
    }

    @Test
    void transactionList401ExpiredToken() {
        enqueueToken();
        server.enqueue(problemResponse(401, "Unauthorized", "Token expired"));

        HttpClient hc = createHttpClient();
        TransactionsResource tx = new TransactionsResource(hc);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () ->
                tx.list(null));
        assertEquals(401, ex.getStatus());
    }

    // ── Documents ─────────────────────────────────────────────

    @Test
    void documentUpload422InvalidCpf() {
        enqueueToken();
        server.enqueue(problemResponse(422, "Unprocessable Entity", "CPF must be exactly 11 digits"));

        HttpClient hc = createHttpClient();
        DocumentsResource docs = new DocumentsResource(hc);

        UnprocessableEntityException ex = assertThrows(UnprocessableEntityException.class, () ->
                docs.upload("tx_1", new com.signdocsbrasil.api.models.UploadDocumentRequest()));
        assertEquals(422, ex.getStatus());
        assertTrue(ex.getProblemDetail().getDetail().contains("CPF"));
    }

    @Test
    void documentConfirm400MissingHash() {
        enqueueToken();
        server.enqueue(problemResponse(400, "Bad Request", "Missing sha256Hash field"));

        HttpClient hc = createHttpClient();
        DocumentsResource docs = new DocumentsResource(hc);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                docs.confirm("tx_1", new com.signdocsbrasil.api.models.ConfirmDocumentRequest()));
        assertEquals(400, ex.getStatus());
    }

    @Test
    void documentDownload404Nonexistent() {
        enqueueToken();
        server.enqueue(problemResponse(404, "Not Found", "Transaction not found"));

        HttpClient hc = createHttpClient();
        DocumentsResource docs = new DocumentsResource(hc);

        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                docs.download("tx-nonexistent"));
        assertEquals(404, ex.getStatus());
    }

    @Test
    void documentPresign400InvalidContentType() {
        enqueueToken();
        server.enqueue(problemResponse(400, "Bad Request", "contentType must be application/pdf"));

        HttpClient hc = createHttpClient();
        DocumentsResource docs = new DocumentsResource(hc);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                docs.presign("tx_1", new com.signdocsbrasil.api.models.PresignRequest("text/plain", "bad.txt")));
        assertEquals(400, ex.getStatus());
    }

    // ── Webhooks ──────────────────────────────────────────────

    @Test
    void webhookRegister400HttpUrl() {
        enqueueToken();
        server.enqueue(problemResponse(400, "Bad Request", "URL must be HTTPS"));

        HttpClient hc = createHttpClient();
        WebhooksResource wh = new WebhooksResource(hc);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                wh.register(new com.signdocsbrasil.api.models.RegisterWebhookRequest()));
        assertEquals(400, ex.getStatus());
        assertTrue(ex.getProblemDetail().getDetail().contains("HTTPS"));
    }

    @Test
    void webhookDelete404Nonexistent() {
        enqueueToken();
        server.enqueue(problemResponse(404, "Not Found", "Webhook not found"));

        HttpClient hc = createHttpClient();
        WebhooksResource wh = new WebhooksResource(hc);

        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                wh.delete("wh-nonexistent"));
        assertEquals(404, ex.getStatus());
    }

    @Test
    void webhookTest400Disabled() {
        enqueueToken();
        server.enqueue(problemResponse(400, "Bad Request", "Webhook is disabled"));

        HttpClient hc = createHttpClient();
        WebhooksResource wh = new WebhooksResource(hc);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                wh.test("wh_1"));
        assertEquals(400, ex.getStatus());
    }

    // ── Steps ─────────────────────────────────────────────────

    @Test
    void stepStart404Nonexistent() {
        enqueueToken();
        server.enqueue(problemResponse(404, "Not Found", "Step step-nonexistent not found"));

        HttpClient hc = createHttpClient();
        StepsResource steps = new StepsResource(hc);

        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                steps.start("tx_1", "step-nonexistent", null));
        assertEquals(404, ex.getStatus());
    }

    @Test
    void stepComplete409AlreadyCompleted() {
        enqueueToken();
        server.enqueue(problemResponse(409, "Conflict", "Step is already completed"));

        HttpClient hc = createHttpClient();
        StepsResource steps = new StepsResource(hc);

        ConflictException ex = assertThrows(ConflictException.class, () ->
                steps.complete("tx_1", "step_1", Collections.singletonMap("accepted", true)));
        assertEquals(409, ex.getStatus());
    }

    @Test
    void stepComplete422WrongOtp() {
        enqueueToken();
        server.enqueue(problemResponse(422, "Unprocessable Entity", "Invalid OTP code"));

        HttpClient hc = createHttpClient();
        StepsResource steps = new StepsResource(hc);

        UnprocessableEntityException ex = assertThrows(UnprocessableEntityException.class, () ->
                steps.complete("tx_1", "step_1", Collections.singletonMap("code", "000000")));
        assertEquals(422, ex.getStatus());
    }

    // ── Signing ───────────────────────────────────────────────

    @Test
    void signingPrepare400EmptyCertChain() {
        enqueueToken();
        server.enqueue(problemResponse(400, "Bad Request", "certificateChainPems must not be empty"));

        HttpClient hc = createHttpClient();
        SigningResource signing = new SigningResource(hc);

        BadRequestException ex = assertThrows(BadRequestException.class, () ->
                signing.prepare("tx_1", new com.signdocsbrasil.api.models.PrepareSigningRequest()));
        assertEquals(400, ex.getStatus());
    }

    @Test
    void signingComplete422InvalidSignature() {
        enqueueToken();
        server.enqueue(problemResponse(422, "Unprocessable Entity", "Invalid raw signature"));

        HttpClient hc = createHttpClient();
        SigningResource signing = new SigningResource(hc);

        UnprocessableEntityException ex = assertThrows(UnprocessableEntityException.class, () ->
                signing.complete("tx_1", new com.signdocsbrasil.api.models.CompleteSigningRequest()));
        assertEquals(422, ex.getStatus());
    }

    // ── Evidence ──────────────────────────────────────────────

    @Test
    void evidenceGet404NoEvidence() {
        enqueueToken();
        server.enqueue(problemResponse(404, "Not Found", "Evidence not found for transaction tx_1"));

        HttpClient hc = createHttpClient();
        EvidenceResource ev = new EvidenceResource(hc);

        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                ev.get("tx_1"));
        assertEquals(404, ex.getStatus());
    }

    @Test
    void evidenceGet403MissingScope() {
        enqueueToken();
        server.enqueue(problemResponse(403, "Forbidden", "Missing required scope: evidence:read"));

        HttpClient hc = createHttpClient();
        EvidenceResource ev = new EvidenceResource(hc);

        ForbiddenException ex = assertThrows(ForbiddenException.class, () ->
                ev.get("tx_1"));
        assertEquals(403, ex.getStatus());
        assertTrue(ex.getProblemDetail().getDetail().contains("evidence:read"));
    }

    // ── Users ─────────────────────────────────────────────────

    @Test
    void userEnroll422InvalidImage() {
        enqueueToken();
        server.enqueue(problemResponse(422, "Unprocessable Entity", "Image must be a valid JPEG base64"));

        HttpClient hc = createHttpClient();
        UsersResource users = new UsersResource(hc);

        UnprocessableEntityException ex = assertThrows(UnprocessableEntityException.class, () ->
                users.enroll("usr_1", new com.signdocsbrasil.api.models.EnrollUserRequest("not-jpeg", "12345678901")));
        assertEquals(422, ex.getStatus());
        assertTrue(ex.getProblemDetail().getDetail().contains("JPEG"));
    }

    // ── Document Groups ───────────────────────────────────────

    @Test
    void documentGroupCombinedStamp404Nonexistent() {
        enqueueToken();
        server.enqueue(problemResponse(404, "Not Found", "Document group not found"));

        HttpClient hc = createHttpClient();
        DocumentGroupsResource grp = new DocumentGroupsResource(hc);

        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                grp.combinedStamp("grp-nonexistent"));
        assertEquals(404, ex.getStatus());
    }

    @Test
    void documentGroupCombinedStamp409NotFullySigned() {
        enqueueToken();
        server.enqueue(problemResponse(409, "Conflict", "Not all signers have completed signing"));

        HttpClient hc = createHttpClient();
        DocumentGroupsResource grp = new DocumentGroupsResource(hc);

        ConflictException ex = assertThrows(ConflictException.class, () ->
                grp.combinedStamp("grp_1"));
        assertEquals(409, ex.getStatus());
    }
}
