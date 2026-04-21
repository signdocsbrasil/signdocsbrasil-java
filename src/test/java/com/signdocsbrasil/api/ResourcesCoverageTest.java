package com.signdocsbrasil.api;

import com.signdocsbrasil.api.models.*;
import com.signdocsbrasil.api.resources.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for resource methods and timeout overloads to ensure coverage.
 */
class ResourcesCoverageTest {

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

    private SignDocsBrasilClient createClient() {
        return SignDocsBrasilClient.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .build();
    }

    // ── Envelopes ──────────────────────────────────────────────

    @Test
    void envelopesCreate() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"envelopeId\":\"env_1\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CreateEnvelopeRequest req = new CreateEnvelopeRequest();
        Envelope env = client.envelopes().create(req);
        assertEquals("env_1", env.getEnvelopeId());
    }

    @Test
    void envelopesCreateWithIdempotencyKey() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"envelopeId\":\"env_2\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CreateEnvelopeRequest req = new CreateEnvelopeRequest();
        Envelope env = client.envelopes().create(req, "idem-key-1");
        assertEquals("env_2", env.getEnvelopeId());
    }

    @Test
    void envelopesCreateWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"envelopeId\":\"env_3\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CreateEnvelopeRequest req = new CreateEnvelopeRequest();
        Envelope env = client.envelopes().create(req, Duration.ofSeconds(5));
        assertEquals("env_3", env.getEnvelopeId());
    }

    @Test
    void envelopesCreateWithIdempotencyAndTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"envelopeId\":\"env_4\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CreateEnvelopeRequest req = new CreateEnvelopeRequest();
        Envelope env = client.envelopes().create(req, "idem-2", Duration.ofSeconds(5));
        assertEquals("env_4", env.getEnvelopeId());
    }

    @Test
    void envelopesGet() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"envelopeId\":\"env_1\",\"status\":\"IN_PROGRESS\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        EnvelopeDetail detail = client.envelopes().get("env_1");
        assertEquals("env_1", detail.getEnvelopeId());
    }

    @Test
    void envelopesGetWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"envelopeId\":\"env_1\",\"status\":\"IN_PROGRESS\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        EnvelopeDetail detail = client.envelopes().get("env_1", Duration.ofSeconds(5));
        assertEquals("env_1", detail.getEnvelopeId());
    }

    @Test
    void envelopesAddSession() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"url\":\"https://sign.example.com\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        AddEnvelopeSessionRequest req = new AddEnvelopeSessionRequest();
        EnvelopeSession session = client.envelopes().addSession("env_1", req);
        assertEquals("sess_1", session.getSessionId());
    }

    @Test
    void envelopesAddSessionWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"url\":\"https://sign.example.com\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        AddEnvelopeSessionRequest req = new AddEnvelopeSessionRequest();
        EnvelopeSession session = client.envelopes().addSession("env_1", req, Duration.ofSeconds(5));
        assertEquals("sess_1", session.getSessionId());
    }

    @Test
    void envelopesCombinedStamp() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"downloadUrl\":\"https://s3.example.com/stamp.pdf\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        EnvelopeCombinedStampResponse resp = client.envelopes().combinedStamp("env_1");
        assertNotNull(resp);
    }

    @Test
    void envelopesCombinedStampWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"downloadUrl\":\"https://s3.example.com/stamp.pdf\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        EnvelopeCombinedStampResponse resp = client.envelopes().combinedStamp("env_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── Transactions timeout overloads ─────────────────────────

    @Test
    void transactionsCreateBasic() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CreateTransactionRequest req = new CreateTransactionRequest();
        Transaction tx = client.transactions().create(req);
        assertEquals("tx_1", tx.getTransactionId());
    }

    @Test
    void transactionsCreateWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        Transaction tx = client.transactions().create(new CreateTransactionRequest(), Duration.ofSeconds(5));
        assertEquals("tx_1", tx.getTransactionId());
    }

    @Test
    void transactionsGet() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"IN_PROGRESS\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        Transaction tx = client.transactions().get("tx_1");
        assertEquals("tx_1", tx.getTransactionId());
    }

    @Test
    void transactionsGetWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"IN_PROGRESS\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        Transaction tx = client.transactions().get("tx_1", Duration.ofSeconds(5));
        assertEquals("tx_1", tx.getTransactionId());
    }

    @Test
    void transactionsListDefault() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[],\"nextCursor\":null}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        TransactionListResponse resp = client.transactions().list();
        assertNotNull(resp);
    }

    @Test
    void transactionsListWithParams() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[],\"nextCursor\":null}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        TransactionListParams params = new TransactionListParams();
        params.setStatus("COMPLETED");
        TransactionListResponse resp = client.transactions().list(params);
        assertNotNull(resp);
    }

    @Test
    void transactionsListWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[],\"nextCursor\":null}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        TransactionListResponse resp = client.transactions().list(null, Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void transactionsCancelWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"CANCELLED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CancelTransactionResponse resp = client.transactions().cancel("tx_1", Duration.ofSeconds(5));
        assertEquals("CANCELLED", resp.getStatus());
    }

    @Test
    void transactionsFinalizeWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"COMPLETED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        FinalizeResponse resp = client.transactions().finalize("tx_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── SigningSessions timeout overloads + missing base ────────

    @Test
    void signingSessionsGetStatus() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"transactionId\":\"tx_1\",\"status\":\"ACTIVE\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        SigningSessionStatus status = client.signingSessions().getStatus("sess_1");
        assertEquals("ACTIVE", status.getStatus());
    }

    @Test
    void signingSessionsGetStatusWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"transactionId\":\"tx_1\",\"status\":\"ACTIVE\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        SigningSessionStatus status = client.signingSessions().getStatus("sess_1", Duration.ofSeconds(5));
        assertEquals("ACTIVE", status.getStatus());
    }

    @Test
    void signingSessionsCancel() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"transactionId\":\"tx_1\",\"status\":\"CANCELLED\",\"cancelledAt\":\"2026-04-08T00:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CancelSigningSessionResponse resp = client.signingSessions().cancel("sess_1");
        assertEquals("CANCELLED", resp.getStatus());
    }

    @Test
    void signingSessionsCancelWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"transactionId\":\"tx_1\",\"status\":\"CANCELLED\",\"cancelledAt\":\"2026-04-08T00:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CancelSigningSessionResponse resp = client.signingSessions().cancel("sess_1", Duration.ofSeconds(5));
        assertEquals("CANCELLED", resp.getStatus());
    }

    @Test
    void signingSessionsListDefault() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessions\":[]}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        SigningSessionListResponse resp = client.signingSessions().list();
        assertNotNull(resp);
    }

    @Test
    void signingSessionsListWithParams() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessions\":[]}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        SigningSessionListParams params = new SigningSessionListParams();
        params.setStatus("COMPLETED");
        SigningSessionListResponse resp = client.signingSessions().list(params);
        assertNotNull(resp);
    }

    @Test
    void signingSessionsListWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessions\":[]}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        SigningSessionListResponse resp = client.signingSessions().list(null, Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── Documents timeout overloads ────────────────────────────

    @Test
    void documentsUpload() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"documentHash\":\"sha256-abc\",\"status\":\"DOCUMENT_UPLOADED\",\"uploadedAt\":\"2026-04-08T00:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        UploadDocumentRequest req = new UploadDocumentRequest();
        DocumentUploadResponse resp = client.documents().upload("tx_1", req);
        assertEquals("tx_1", resp.getTransactionId());
    }

    @Test
    void documentsPresignWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"uploadUrl\":\"https://s3.example.com\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        PresignResponse resp = client.documents().presign("tx_1", new PresignRequest(), Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void documentsDownloadWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"downloadUrl\":\"https://s3.example.com/doc.pdf\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        DownloadResponse resp = client.documents().download("tx_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── Health timeout overloads ───────────────────────────────

    @Test
    void healthCheckWithTimeout() {
        server.enqueue(new MockResponse()
                .setBody("{\"status\":\"healthy\"}")
                .setHeader("Content-Type", "application/json"));

        Config config = Config.builder()
                .clientId("test").clientSecret("test")
                .baseUrl(baseUrl()).maxRetries(0).build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient http = new HttpClient(config, auth);
        HealthResource health = new HealthResource(http);
        HealthCheckResponse resp = health.check(Duration.ofSeconds(5));
        assertEquals("healthy", resp.getStatus());
    }

    @Test
    void healthHistory() {
        server.enqueue(new MockResponse()
                .setBody("{\"entries\":[]}")
                .setHeader("Content-Type", "application/json"));

        Config config = Config.builder()
                .clientId("test").clientSecret("test")
                .baseUrl(baseUrl()).maxRetries(0).build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient http = new HttpClient(config, auth);
        HealthResource health = new HealthResource(http);
        HealthHistoryResponse resp = health.history();
        assertNotNull(resp);
    }

    // ── Webhooks missing methods ───────────────────────────────

    @Test
    void webhooksList() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"webhooks\":[{\"webhookId\":\"wh_1\"}],\"count\":1}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        Object webhooks = client.webhooks().list();
        assertNotNull(webhooks);
    }

    @Test
    void webhooksTest() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"success\":true}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        WebhookTestResponse resp = client.webhooks().test("wh_1");
        assertNotNull(resp);
    }

    // ── Steps/Signing/Evidence/Verification/Users/DocGroups timeout overloads ──

    @Test
    void stepsListWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"steps\":[]}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        StepListResponse resp = client.steps().list("tx_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void signingPrepareWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"signatureRequestId\":\"sr_1\",\"hashToSign\":\"abc\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        PrepareSigningResponse resp = client.signing().prepare("tx_1", new PrepareSigningRequest(), Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void signingCompleteWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"COMPLETED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CompleteSigningResponse resp = client.signing().complete("tx_1", new CompleteSigningRequest(), Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void evidenceGetWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"evidenceId\":\"ev_1\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        Evidence resp = client.evidence().get("tx_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void verificationVerifyWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"valid\":true}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        VerificationResponse resp = client.verification().verify("ev_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void verificationDownloadsWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"files\":[]}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        VerificationDownloadsResponse resp = client.verification().downloads("ev_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void usersEnrollWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"userId\":\"u_1\",\"status\":\"ENROLLED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        EnrollUserResponse resp = client.users().enroll("tx_1", new EnrollUserRequest(), Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── Transactions create overloads ──────────────────────────

    @Test
    void transactionsCreateWithIdempotencyKey() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        Transaction tx = client.transactions().create(new CreateTransactionRequest(), "idem-1");
        assertEquals("tx_1", tx.getTransactionId());
    }

    @Test
    void transactionsCreateWithIdempotencyAndTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        Transaction tx = client.transactions().create(new CreateTransactionRequest(), "idem-1", Duration.ofSeconds(5));
        assertEquals("tx_1", tx.getTransactionId());
    }

    @Test
    void transactionsListWithParamsAndTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactions\":[],\"nextCursor\":null}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        TransactionListParams params = new TransactionListParams();
        params.setStatus("COMPLETED");
        TransactionListResponse resp = client.transactions().list(params, Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── Documents upload with timeout ──────────────────────────

    @Test
    void documentsUploadWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"documentHash\":\"sha256-abc\",\"status\":\"DOCUMENT_UPLOADED\",\"uploadedAt\":\"2026-04-08T00:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        DocumentUploadResponse resp = client.documents().upload("tx_1", new UploadDocumentRequest(), Duration.ofSeconds(5));
        assertEquals("tx_1", resp.getTransactionId());
    }

    @Test
    void documentsConfirmWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"transactionId\":\"tx_1\",\"status\":\"CONFIRMED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        ConfirmDocumentResponse resp = client.documents().confirm("tx_1", new ConfirmDocumentRequest(), Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── Steps start/complete overloads ──────────────────────────

    @Test
    void stepsStartWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"stepId\":\"s1\",\"status\":\"STARTED\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        StartStepResponse resp = client.steps().start("tx_1", "s1", null, Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void stepsCompleteWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"stepId\":\"s1\",\"type\":\"CLICK_ACCEPT\",\"status\":\"COMPLETED\",\"attempts\":1}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        StepCompleteResponse resp = client.steps().complete("tx_1", "s1", null, Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── Webhooks timeout + missing overloads ────────────────────

    @Test
    void webhooksRegisterWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"webhookId\":\"wh_1\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        RegisterWebhookResponse resp = client.webhooks().register(new RegisterWebhookRequest(), Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    @Test
    void webhooksDeleteWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse().setResponseCode(204));

        SignDocsBrasilClient client = createClient();
        client.webhooks().delete("wh_1", Duration.ofSeconds(5));
    }

    @Test
    void webhooksListWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"webhooks\":[{\"webhookId\":\"wh_1\"}],\"count\":1}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        Object result = client.webhooks().list(Duration.ofSeconds(5));
        assertNotNull(result);
    }

    @Test
    void webhooksTestWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"success\":true}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        WebhookTestResponse resp = client.webhooks().test("wh_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }

    // ── SigningSessions create overloads ─────────────────────────

    @Test
    void signingSessionsCreateWithIdempotencyKey() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"transactionId\":\"tx_1\",\"status\":\"ACTIVE\",\"url\":\"https://sign.example.com\",\"clientSecret\":\"cs_1\",\"expiresAt\":\"2026-04-10T00:00:00Z\",\"createdAt\":\"2026-04-08T00:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        SigningSession sess = client.signingSessions().create(new CreateSigningSessionRequest(), "idem-1");
        assertEquals("sess_1", sess.getSessionId());
    }

    @Test
    void signingSessionsCreateWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"transactionId\":\"tx_1\",\"status\":\"ACTIVE\",\"url\":\"https://sign.example.com\",\"clientSecret\":\"cs_1\",\"expiresAt\":\"2026-04-10T00:00:00Z\",\"createdAt\":\"2026-04-08T00:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        SigningSession sess = client.signingSessions().create(new CreateSigningSessionRequest(), Duration.ofSeconds(5));
        assertEquals("sess_1", sess.getSessionId());
    }

    @Test
    void signingSessionsCreateWithIdempotencyAndTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"transactionId\":\"tx_1\",\"status\":\"ACTIVE\",\"url\":\"https://sign.example.com\",\"clientSecret\":\"cs_1\",\"expiresAt\":\"2026-04-10T00:00:00Z\",\"createdAt\":\"2026-04-08T00:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        SigningSession sess = client.signingSessions().create(new CreateSigningSessionRequest(), "idem-1", Duration.ofSeconds(5));
        assertEquals("sess_1", sess.getSessionId());
    }

    @Test
    void documentGroupsCombinedStampWithTimeout() {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"downloadUrl\":\"https://s3.example.com\"}")
                .setHeader("Content-Type", "application/json"));

        SignDocsBrasilClient client = createClient();
        CombinedStampResponse resp = client.documentGroups().combinedStamp("dg_1", Duration.ofSeconds(5));
        assertNotNull(resp);
    }
}
