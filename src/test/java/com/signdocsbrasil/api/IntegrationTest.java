package com.signdocsbrasil.api;

import com.signdocsbrasil.api.errors.*;
import com.signdocsbrasil.api.models.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests that exercise the full SDK stack:
 * Config -> Client -> AuthHandler -> HttpClient -> RetryHandler -> Resource -> Model deserialization.
 *
 * Uses OkHttp MockWebServer to simulate API responses with fixture data.
 * Each test creates a real SignDocsBrasilClient pointed at the mock server,
 * calls a resource method, and verifies the returned model fields match fixture data.
 */
class IntegrationTest {

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

    // -- Helper methods --

    private String baseUrl() {
        return server.url("").toString().replaceAll("/$", "");
    }

    private void enqueueToken() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.fake\",\"token_type\":\"Bearer\",\"expires_in\":900,\"scope\":\"transactions:read transactions:write\"}")
                .setHeader("Content-Type", "application/json"));
    }

    private SignDocsBrasilClient createClient() {
        return SignDocsBrasilClient.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .build();
    }

    // -- Fixture response bodies --

    private static final String TRANSACTIONS_CREATE_BODY = "{"
            + "\"tenantId\":\"abc123\","
            + "\"transactionId\":\"tx-uuid-001\","
            + "\"status\":\"CREATED\","
            + "\"purpose\":\"DOCUMENT_SIGNATURE\","
            + "\"policy\":{\"profile\":\"CLICK_ONLY\"},"
            + "\"signer\":{\"name\":\"Jo\\u00e3o Silva\",\"email\":\"joao@example.com\",\"userExternalId\":\"user-ext-001\",\"cpf\":\"12345678901\"},"
            + "\"steps\":[{\"tenantId\":\"abc123\",\"transactionId\":\"tx-uuid-001\",\"stepId\":\"step-uuid-001\",\"type\":\"CLICK_ACCEPT\",\"status\":\"PENDING\",\"order\":1,\"attempts\":0,\"maxAttempts\":3}],"
            + "\"metadata\":{\"contractId\":\"CTR-2024-001\"},"
            + "\"expiresAt\":\"2024-11-16T00:00:00.000Z\","
            + "\"createdAt\":\"2024-11-15T00:00:00.000Z\","
            + "\"updatedAt\":\"2024-11-15T00:00:00.000Z\""
            + "}";

    private static final String TRANSACTIONS_LIST_BODY = "{"
            + "\"transactions\":["
            + "{\"tenantId\":\"abc123\",\"transactionId\":\"tx-uuid-002\",\"status\":\"COMPLETED\",\"purpose\":\"DOCUMENT_SIGNATURE\","
            + "\"policy\":{\"profile\":\"CLICK_ONLY\"},"
            + "\"signer\":{\"name\":\"Maria Santos\",\"email\":\"maria@example.com\",\"userExternalId\":\"user-ext-002\"},"
            + "\"steps\":[],"
            + "\"expiresAt\":\"2024-11-17T00:00:00.000Z\",\"createdAt\":\"2024-11-14T10:00:00.000Z\",\"updatedAt\":\"2024-11-14T10:05:00.000Z\"},"
            + "{\"tenantId\":\"abc123\",\"transactionId\":\"tx-uuid-003\",\"status\":\"COMPLETED\",\"purpose\":\"DOCUMENT_SIGNATURE\","
            + "\"policy\":{\"profile\":\"BIOMETRIC\"},"
            + "\"signer\":{\"name\":\"Pedro Costa\",\"email\":\"pedro@example.com\",\"userExternalId\":\"user-ext-003\"},"
            + "\"steps\":[],"
            + "\"expiresAt\":\"2024-11-18T00:00:00.000Z\",\"createdAt\":\"2024-11-14T11:00:00.000Z\",\"updatedAt\":\"2024-11-14T11:30:00.000Z\"}"
            + "],"
            + "\"nextToken\":\"eyJQSyI6IlRFTkFOVCNhYmMxMjMiLCJTSyI6IlRYI3R4LXV1aWQtMDAzIn0=\","
            + "\"count\":2"
            + "}";

    private static final String TRANSACTIONS_GET_BODY = "{"
            + "\"tenantId\":\"abc123\","
            + "\"transactionId\":\"tx-uuid-001\","
            + "\"status\":\"IN_PROGRESS\","
            + "\"purpose\":\"DOCUMENT_SIGNATURE\","
            + "\"policy\":{\"profile\":\"CLICK_PLUS_OTP\"},"
            + "\"signer\":{\"name\":\"Jo\\u00e3o Silva\",\"email\":\"joao@example.com\",\"userExternalId\":\"user-ext-001\",\"cpf\":\"12345678901\"},"
            + "\"steps\":["
            + "{\"tenantId\":\"abc123\",\"transactionId\":\"tx-uuid-001\",\"stepId\":\"step-uuid-001\",\"type\":\"CLICK_ACCEPT\",\"status\":\"COMPLETED\",\"order\":1,\"attempts\":1,\"maxAttempts\":3,\"completedAt\":\"2024-11-15T00:01:00.000Z\",\"result\":{\"click\":{\"accepted\":true,\"textVersion\":\"v1.0\"}}},"
            + "{\"tenantId\":\"abc123\",\"transactionId\":\"tx-uuid-001\",\"stepId\":\"step-uuid-002\",\"type\":\"OTP_CHALLENGE\",\"status\":\"PENDING\",\"order\":2,\"attempts\":0,\"maxAttempts\":3}"
            + "],"
            + "\"expiresAt\":\"2024-11-16T00:00:00.000Z\","
            + "\"createdAt\":\"2024-11-15T00:00:00.000Z\","
            + "\"updatedAt\":\"2024-11-15T00:01:00.000Z\""
            + "}";

    private static final String DOCUMENTS_UPLOAD_BODY = "{"
            + "\"transactionId\":\"tx-uuid-001\","
            + "\"documentHash\":\"sha256-abc123\","
            + "\"status\":\"DOCUMENT_UPLOADED\","
            + "\"uploadedAt\":\"2024-11-15T00:00:30.000Z\""
            + "}";

    private static final String WEBHOOKS_REGISTER_BODY = "{"
            + "\"webhookId\":\"wh-uuid-001\","
            + "\"url\":\"https://example.com/webhooks/signdocs\","
            + "\"secret\":\"whsec_generated_secret_abc123\","
            + "\"events\":[\"TRANSACTION.COMPLETED\",\"TRANSACTION.FAILED\"],"
            + "\"status\":\"ACTIVE\","
            + "\"createdAt\":\"2024-11-15T00:00:00.000Z\""
            + "}";

    private static final String HEALTH_CHECK_BODY = "{"
            + "\"status\":\"healthy\","
            + "\"version\":\"1.0.0\","
            + "\"timestamp\":\"2024-11-15T12:00:00.000Z\","
            + "\"services\":{"
            + "\"dynamodb\":{\"status\":\"healthy\",\"latency\":12},"
            + "\"s3\":{\"status\":\"healthy\",\"latency\":8},"
            + "\"cognito\":{\"status\":\"healthy\",\"latency\":15}"
            + "}"
            + "}";

    private static final String VERIFICATION_VERIFY_BODY = "{"
            + "\"evidenceId\":\"ev-uuid-001\","
            + "\"transactionId\":\"tx-uuid-001\","
            + "\"status\":\"COMPLETED\","
            + "\"signer\":{\"displayName\":\"Jo\\u00e3o Silva\"},"
            + "\"completedAt\":\"2024-11-15T00:01:00.000Z\""
            + "}";

    private static final String ERROR_400_BODY = "{"
            + "\"type\":\"https://api.signdocs.com.br/errors/bad-request\","
            + "\"title\":\"Bad Request\","
            + "\"status\":400,"
            + "\"detail\":\"Invalid policy profile: UNKNOWN_PROFILE\","
            + "\"instance\":\"/v1/transactions\""
            + "}";

    private static final String ERROR_404_BODY = "{"
            + "\"type\":\"https://api.signdocs.com.br/errors/not-found\","
            + "\"title\":\"Not Found\","
            + "\"status\":404,"
            + "\"detail\":\"Transaction tx-nonexistent not found\""
            + "}";

    private static final String ERROR_429_BODY = "{"
            + "\"type\":\"https://api.signdocs.com.br/errors/rate-limit\","
            + "\"title\":\"Too Many Requests\","
            + "\"status\":429,"
            + "\"detail\":\"Daily transaction quota exceeded\""
            + "}";

    private static final String ERROR_409_BODY = "{"
            + "\"type\":\"https://api.signdocs.com.br/errors/conflict\","
            + "\"title\":\"Conflict\","
            + "\"status\":409,"
            + "\"detail\":\"Transaction tx-uuid-001 is already finalized\""
            + "}";

    // ====================================================
    // Happy path tests
    // ====================================================

    @Test
    void transactionsCreate() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(TRANSACTIONS_CREATE_BODY)
                .setHeader("Content-Type", "application/json"));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setPurpose("DOCUMENT_SIGNATURE");
        request.setPolicy(new Policy("CLICK_ONLY"));
        Signer signer = new Signer();
        signer.setName("Jo\u00e3o Silva");
        signer.setEmail("joao@example.com");
        signer.setUserExternalId("user-ext-001");
        signer.setCpf("12345678901");
        request.setSigner(signer);

        Transaction tx = client.transactions().create(request);

        // Verify model deserialization
        assertEquals("abc123", tx.getTenantId());
        assertEquals("tx-uuid-001", tx.getTransactionId());
        assertEquals("CREATED", tx.getStatus());
        assertEquals("DOCUMENT_SIGNATURE", tx.getPurpose());
        assertEquals("CLICK_ONLY", tx.getPolicy().getProfile());
        assertEquals("Jo\u00e3o Silva", tx.getSigner().getName());
        assertEquals("joao@example.com", tx.getSigner().getEmail());
        assertEquals("user-ext-001", tx.getSigner().getUserExternalId());
        assertEquals("12345678901", tx.getSigner().getCpf());
        assertNotNull(tx.getSteps());
        assertEquals(1, tx.getSteps().size());
        assertEquals("step-uuid-001", tx.getSteps().get(0).getStepId());
        assertEquals("CLICK_ACCEPT", tx.getSteps().get(0).getType());
        assertEquals("PENDING", tx.getSteps().get(0).getStatus());
        assertEquals(1, tx.getSteps().get(0).getOrder());
        assertEquals(0, tx.getSteps().get(0).getAttempts());
        assertEquals(3, tx.getSteps().get(0).getMaxAttempts());
        assertEquals("CTR-2024-001", tx.getMetadata().get("contractId"));
        assertEquals("2024-11-16T00:00:00.000Z", tx.getExpiresAt());
        assertEquals("2024-11-15T00:00:00.000Z", tx.getCreatedAt());
        assertEquals("2024-11-15T00:00:00.000Z", tx.getUpdatedAt());

        // Verify HTTP request was correct
        RecordedRequest tokenReq = server.takeRequest();
        assertTrue(tokenReq.getPath().contains("/oauth2/token"));

        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertTrue(apiReq.getPath().startsWith("/v1/transactions"));
        assertNotNull(apiReq.getHeader("Authorization"));
        assertTrue(apiReq.getHeader("Authorization").startsWith("Bearer "));
        assertNotNull(apiReq.getHeader("X-Idempotency-Key"));
        assertTrue(apiReq.getHeader("Content-Type").contains("application/json"));
    }

    @Test
    void transactionsList() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TRANSACTIONS_LIST_BODY)
                .setHeader("Content-Type", "application/json"));

        TransactionListParams params = new TransactionListParams();
        params.setStatus("COMPLETED");
        params.setLimit(2);

        TransactionListResponse response = client.transactions().list(params);

        // Verify pagination
        assertEquals(2, response.getCount());
        assertEquals("eyJQSyI6IlRFTkFOVCNhYmMxMjMiLCJTSyI6IlRYI3R4LXV1aWQtMDAzIn0=", response.getNextToken());

        // Verify transactions list
        List<Transaction> txs = response.getTransactions();
        assertNotNull(txs);
        assertEquals(2, txs.size());

        // First transaction
        Transaction tx1 = txs.get(0);
        assertEquals("tx-uuid-002", tx1.getTransactionId());
        assertEquals("COMPLETED", tx1.getStatus());
        assertEquals("Maria Santos", tx1.getSigner().getName());
        assertEquals("CLICK_ONLY", tx1.getPolicy().getProfile());

        // Second transaction
        Transaction tx2 = txs.get(1);
        assertEquals("tx-uuid-003", tx2.getTransactionId());
        assertEquals("COMPLETED", tx2.getStatus());
        assertEquals("Pedro Costa", tx2.getSigner().getName());
        assertEquals("BIOMETRIC", tx2.getPolicy().getProfile());

        // Verify query parameters
        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("GET", apiReq.getMethod());
        assertTrue(apiReq.getPath().contains("status=COMPLETED"));
        assertTrue(apiReq.getPath().contains("limit=2"));
    }

    @Test
    void transactionsGet() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TRANSACTIONS_GET_BODY)
                .setHeader("Content-Type", "application/json"));

        Transaction tx = client.transactions().get("tx-uuid-001");

        // Verify top-level fields
        assertEquals("abc123", tx.getTenantId());
        assertEquals("tx-uuid-001", tx.getTransactionId());
        assertEquals("IN_PROGRESS", tx.getStatus());
        assertEquals("CLICK_PLUS_OTP", tx.getPolicy().getProfile());

        // Verify nested steps
        assertNotNull(tx.getSteps());
        assertEquals(2, tx.getSteps().size());

        // First step - completed
        Step step1 = tx.getSteps().get(0);
        assertEquals("step-uuid-001", step1.getStepId());
        assertEquals("CLICK_ACCEPT", step1.getType());
        assertEquals("COMPLETED", step1.getStatus());
        assertEquals(1, step1.getOrder());
        assertEquals(1, step1.getAttempts());
        assertEquals("2024-11-15T00:01:00.000Z", step1.getCompletedAt());
        assertNotNull(step1.getResult());

        // Second step - pending
        Step step2 = tx.getSteps().get(1);
        assertEquals("step-uuid-002", step2.getStepId());
        assertEquals("OTP_CHALLENGE", step2.getType());
        assertEquals("PENDING", step2.getStatus());
        assertEquals(2, step2.getOrder());
        assertEquals(0, step2.getAttempts());
        assertNull(step2.getCompletedAt());

        // Verify HTTP request path
        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("GET", apiReq.getMethod());
        assertEquals("/v1/transactions/tx-uuid-001", apiReq.getPath());
    }

    @Test
    void documentsUpload() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(DOCUMENTS_UPLOAD_BODY)
                .setHeader("Content-Type", "application/json"));

        UploadDocumentRequest request = new UploadDocumentRequest(
                "JVBERi0xLjQKMSAwIG9iago8PAovVHlwZQ==", "contract.pdf");

        DocumentUploadResponse uploadResp = client.documents().upload("tx-uuid-001", request);

        // Verify returned response reflects document upload
        assertEquals("tx-uuid-001", uploadResp.getTransactionId());
        assertEquals("DOCUMENT_UPLOADED", uploadResp.getStatus());
        assertEquals("sha256-abc123", uploadResp.getDocumentHash());
        assertEquals("2024-11-15T00:00:30.000Z", uploadResp.getUploadedAt());

        // Verify HTTP request
        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertEquals("/v1/transactions/tx-uuid-001/document", apiReq.getPath());
        String body = apiReq.getBody().readUtf8();
        // Gson HTML-safe mode escapes '=' as '\u003d'
        assertTrue(body.contains("JVBERi0xLjQKMSAwIG9iago8PAovVHlwZQ"));
        assertTrue(body.contains("contract.pdf"));
    }

    @Test
    void webhooksRegister() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(WEBHOOKS_REGISTER_BODY)
                .setHeader("Content-Type", "application/json"));

        RegisterWebhookRequest request = new RegisterWebhookRequest(
                "https://example.com/webhooks/signdocs",
                Arrays.asList("TRANSACTION.COMPLETED", "TRANSACTION.FAILED"));

        RegisterWebhookResponse response = client.webhooks().register(request);

        // Verify all response fields
        assertEquals("wh-uuid-001", response.getWebhookId());
        assertEquals("https://example.com/webhooks/signdocs", response.getUrl());
        assertEquals("whsec_generated_secret_abc123", response.getSecret());
        assertNotNull(response.getEvents());
        assertEquals(2, response.getEvents().size());
        assertEquals("TRANSACTION.COMPLETED", response.getEvents().get(0));
        assertEquals("TRANSACTION.FAILED", response.getEvents().get(1));
        assertEquals("ACTIVE", response.getStatus());
        assertEquals("2024-11-15T00:00:00.000Z", response.getCreatedAt());

        // Verify HTTP request
        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertEquals("/v1/webhooks", apiReq.getPath());
        assertNotNull(apiReq.getHeader("Authorization"));
    }

    @Test
    void healthCheck() throws Exception {
        SignDocsBrasilClient client = createClient();
        // No token enqueue needed -- health is noAuth
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(HEALTH_CHECK_BODY)
                .setHeader("Content-Type", "application/json"));

        HealthCheckResponse response = client.health().check();

        // Verify response fields
        assertEquals("healthy", response.getStatus());
        assertEquals("1.0.0", response.getVersion());
        assertEquals("2024-11-15T12:00:00.000Z", response.getTimestamp());

        // Verify services map
        assertNotNull(response.getServices());
        assertEquals(3, response.getServices().size());
        assertEquals("healthy", response.getServices().get("dynamodb").getStatus());
        assertEquals(12.0, response.getServices().get("dynamodb").getLatency());
        assertEquals("healthy", response.getServices().get("s3").getStatus());
        assertEquals(8.0, response.getServices().get("s3").getLatency());
        assertEquals("healthy", response.getServices().get("cognito").getStatus());
        assertEquals(15.0, response.getServices().get("cognito").getLatency());

        // Verify no auth header was sent
        RecordedRequest req = server.takeRequest();
        assertNull(req.getHeader("Authorization"));
        assertEquals("GET", req.getMethod());
        assertEquals("/health", req.getPath());
    }

    @Test
    void verificationVerify() throws Exception {
        SignDocsBrasilClient client = createClient();
        // No token enqueue needed -- verification is noAuth
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(VERIFICATION_VERIFY_BODY)
                .setHeader("Content-Type", "application/json"));

        VerificationResponse response = client.verification().verify("ev-uuid-001");

        // Verify response fields
        assertEquals("ev-uuid-001", response.getEvidenceId());
        assertEquals("tx-uuid-001", response.getTransactionId());
        assertEquals("COMPLETED", response.getStatus());
        assertNotNull(response.getSigner());
        assertEquals("Jo\u00e3o Silva", response.getSigner().getDisplayName());
        assertEquals("2024-11-15T00:01:00.000Z", response.getCompletedAt());

        // Verify no auth header was sent
        RecordedRequest req = server.takeRequest();
        assertNull(req.getHeader("Authorization"));
        assertEquals("GET", req.getMethod());
        assertEquals("/v1/verify/ev-uuid-001", req.getPath());
    }

    // ====================================================
    // Error path tests
    // ====================================================

    @Test
    void error400BadRequest() {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody(ERROR_400_BODY)
                .setHeader("Content-Type", "application/problem+json"));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setPurpose("DOCUMENT_SIGNATURE");
        request.setPolicy(new Policy("UNKNOWN_PROFILE"));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> client.transactions().create(request));

        // Verify ProblemDetail fields
        assertEquals(400, ex.getStatus());
        assertEquals("https://api.signdocs.com.br/errors/bad-request", ex.getType());
        assertEquals("Bad Request", ex.getTitle());
        assertEquals("Invalid policy profile: UNKNOWN_PROFILE", ex.getDetail());
        assertEquals("/v1/transactions", ex.getInstance());

        ProblemDetail pd = ex.getProblemDetail();
        assertNotNull(pd);
        assertEquals(400, pd.getStatus());
    }

    @Test
    void error404NotFound() {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody(ERROR_404_BODY)
                .setHeader("Content-Type", "application/problem+json"));

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> client.transactions().get("tx-nonexistent"));

        assertEquals(404, ex.getStatus());
        assertEquals("https://api.signdocs.com.br/errors/not-found", ex.getType());
        assertEquals("Not Found", ex.getTitle());
        assertEquals("Transaction tx-nonexistent not found", ex.getDetail());
    }

    @Test
    void error429RateLimit() {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(429)
                .setBody(ERROR_429_BODY)
                .setHeader("Content-Type", "application/problem+json")
                .setHeader("Retry-After", "5"));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setPurpose("DOCUMENT_SIGNATURE");

        RateLimitException ex = assertThrows(RateLimitException.class,
                () -> client.transactions().create(request));

        assertEquals(429, ex.getStatus());
        assertEquals("https://api.signdocs.com.br/errors/rate-limit", ex.getType());
        assertEquals("Too Many Requests", ex.getTitle());
        assertEquals("Daily transaction quota exceeded", ex.getDetail());
        assertNotNull(ex.getRetryAfterSeconds());
        assertEquals(5, ex.getRetryAfterSeconds());
    }

    @Test
    void error409Conflict() {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(409)
                .setBody(ERROR_409_BODY)
                .setHeader("Content-Type", "application/problem+json"));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setPurpose("DOCUMENT_SIGNATURE");

        ConflictException ex = assertThrows(ConflictException.class,
                () -> client.transactions().create(request));

        assertEquals(409, ex.getStatus());
        assertEquals("https://api.signdocs.com.br/errors/conflict", ex.getType());
        assertEquals("Conflict", ex.getTitle());
        assertEquals("Transaction tx-uuid-001 is already finalized", ex.getDetail());
    }
}
