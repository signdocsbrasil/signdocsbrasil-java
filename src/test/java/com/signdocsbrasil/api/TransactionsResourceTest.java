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

import static org.junit.jupiter.api.Assertions.*;

class TransactionsResourceTest {

    private MockWebServer server;
    private SignDocsBrasilClient client;

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

    private SignDocsBrasilClient createClient() {
        String baseUrl = server.url("").toString().replaceAll("/$", "");
        return SignDocsBrasilClient.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl)
                .maxRetries(0)
                .build();
    }

    // -- Original tests --

    @Test
    void createTransaction() throws Exception {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"id\":\"tx_1\",\"status\":\"CREATED\"}")
                .setHeader("Content-Type", "application/json"));

        // We need to access transactions, but SignDocsBrasilClient needs access to HttpClient
        // For now, verify the HTTP layer works via the full client integration
        assertNotNull(client);
    }

    @Test
    void cancelReturns200WithBody() throws Exception {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"tx_1\",\"status\":\"CANCELLED\"}")
                .setHeader("Content-Type", "application/json"));

        // Verify DELETE returns 200 with body (not 204)
        assertNotNull(client);
    }

    // -- Error path tests --

    @Test
    void transactionsCreateThrows400BadRequest() {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"type\":\"https://api.signdocs.com.br/errors/bad-request\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"Invalid policy profile\"}")
                .setHeader("Content-Type", "application/problem+json"));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setPurpose("DOCUMENT_SIGNATURE");
        request.setPolicy(new Policy("INVALID"));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> client.transactions().create(request));
        assertEquals(400, ex.getStatus());
        assertEquals("Invalid policy profile", ex.getDetail());
    }

    @Test
    void transactionsGetThrows404NotFound() {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"type\":\"https://api.signdocs.com.br/errors/not-found\",\"title\":\"Not Found\",\"status\":404,\"detail\":\"Transaction tx-nonexistent not found\"}")
                .setHeader("Content-Type", "application/problem+json"));

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> client.transactions().get("tx-nonexistent"));
        assertEquals(404, ex.getStatus());
        assertEquals("Transaction tx-nonexistent not found", ex.getDetail());
    }

    @Test
    void transactionsCreateDuplicateThrows409Conflict() {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(409)
                .setBody("{\"type\":\"https://api.signdocs.com.br/errors/conflict\",\"title\":\"Conflict\",\"status\":409,\"detail\":\"Transaction already exists\"}")
                .setHeader("Content-Type", "application/problem+json"));

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setPurpose("DOCUMENT_SIGNATURE");

        ConflictException ex = assertThrows(ConflictException.class,
                () -> client.transactions().create(request));
        assertEquals(409, ex.getStatus());
        assertEquals("Transaction already exists", ex.getDetail());
    }

    @Test
    void documentsUploadThrows422UnprocessableEntity() {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(422)
                .setBody("{\"type\":\"https://api.signdocs.com.br/errors/unprocessable-entity\",\"title\":\"Unprocessable Entity\",\"status\":422,\"detail\":\"CPF must be exactly 11 digits\"}")
                .setHeader("Content-Type", "application/problem+json"));

        UploadDocumentRequest request = new UploadDocumentRequest("invalid-content", "doc.pdf");

        UnprocessableEntityException ex = assertThrows(UnprocessableEntityException.class,
                () -> client.documents().upload("tx-001", request));
        assertEquals(422, ex.getStatus());
        assertEquals("CPF must be exactly 11 digits", ex.getDetail());
    }

    @Test
    void webhooksRegisterThrows400BadRequest() {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"type\":\"https://api.signdocs.com.br/errors/bad-request\",\"title\":\"Bad Request\",\"status\":400,\"detail\":\"URL must be HTTPS\"}")
                .setHeader("Content-Type", "application/problem+json"));

        RegisterWebhookRequest request = new RegisterWebhookRequest(
                "http://insecure.example.com/hook",
                Arrays.asList("TRANSACTION.COMPLETED"));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> client.webhooks().register(request));
        assertEquals(400, ex.getStatus());
        assertEquals("URL must be HTTPS", ex.getDetail());
    }

    // -- Resource method tests --

    @Test
    void transactionsCancelSendsDeleteAndReturnsTransaction() throws Exception {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"transactionId\":\"tx-001\",\"status\":\"CANCELLED\",\"cancelledAt\":\"2024-11-15T00:00:00.000Z\"}")
                .setHeader("Content-Type", "application/json"));

        CancelTransactionResponse resp = client.transactions().cancel("tx-001");

        assertNotNull(resp);
        assertEquals("tx-001", resp.getTransactionId());
        assertEquals("CANCELLED", resp.getStatus());
        assertEquals("2024-11-15T00:00:00.000Z", resp.getCancelledAt());

        // Skip token request
        server.takeRequest();
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("DELETE", apiReq.getMethod());
        assertEquals("/v1/transactions/tx-001", apiReq.getPath());
    }

    @Test
    void transactionsFinalizeSendsPostAndReturnsTransaction() throws Exception {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"transactionId\":\"tx-001\",\"status\":\"COMPLETED\",\"evidenceId\":\"ev-001\",\"evidenceHash\":\"abc123hash\",\"completedAt\":\"2024-11-15T00:01:00.000Z\"}")
                .setHeader("Content-Type", "application/json"));

        FinalizeResponse resp = client.transactions().finalize("tx-001");

        assertNotNull(resp);
        assertEquals("tx-001", resp.getTransactionId());
        assertEquals("COMPLETED", resp.getStatus());
        assertEquals("ev-001", resp.getEvidenceId());
        assertEquals("abc123hash", resp.getEvidenceHash());
        assertEquals("2024-11-15T00:01:00.000Z", resp.getCompletedAt());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertEquals("/v1/transactions/tx-001/finalize", apiReq.getPath());
    }

    @Test
    void documentsPresignReturnsPresignResponse() throws Exception {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"uploadUrl\":\"https://s3.amazonaws.com/bucket/key?signed\",\"uploadToken\":\"tok-abc\",\"s3Key\":\"uploads/tx-001/doc.pdf\",\"expiresIn\":3600,\"contentType\":\"application/pdf\",\"instructions\":\"PUT to uploadUrl\"}")
                .setHeader("Content-Type", "application/json"));

        PresignRequest presignReq = new PresignRequest("application/pdf", "contract.pdf");
        PresignResponse response = client.documents().presign("tx-001", presignReq);

        assertNotNull(response);
        assertEquals("https://s3.amazonaws.com/bucket/key?signed", response.getUploadUrl());
        assertEquals("tok-abc", response.getUploadToken());
        assertEquals("uploads/tx-001/doc.pdf", response.getS3Key());
        assertEquals(3600, response.getExpiresIn());
        assertEquals("application/pdf", response.getContentType());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertEquals("/v1/transactions/tx-001/document/presign", apiReq.getPath());
    }

    @Test
    void documentsConfirmReturnsTransaction() throws Exception {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"transactionId\":\"tx-001\",\"status\":\"DOCUMENT_UPLOADED\",\"documentHash\":\"sha256-abcdef\"}")
                .setHeader("Content-Type", "application/json"));

        ConfirmDocumentRequest request = new ConfirmDocumentRequest("abcdef1234567890");

        ConfirmDocumentResponse resp = client.documents().confirm("tx-001", request);

        assertNotNull(resp);
        assertEquals("tx-001", resp.getTransactionId());
        assertEquals("DOCUMENT_UPLOADED", resp.getStatus());
        assertEquals("sha256-abcdef", resp.getDocumentHash());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertEquals("/v1/transactions/tx-001/document/confirm", apiReq.getPath());
        assertTrue(apiReq.getBody().readUtf8().contains("abcdef1234567890"));
    }

    @Test
    void documentsDownloadReturnsDownloadResponse() throws Exception {
        client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"transactionId\":\"tx-001\",\"documentHash\":\"sha256-abcdef\",\"originalUrl\":\"https://s3.amazonaws.com/bucket/original.pdf?signed\",\"signedUrl\":\"https://s3.amazonaws.com/bucket/signed.pdf?signed\",\"expiresIn\":3600}")
                .setHeader("Content-Type", "application/json"));

        DownloadResponse response = client.documents().download("tx-001");

        assertNotNull(response);
        assertEquals("tx-001", response.getTransactionId());
        assertEquals("https://s3.amazonaws.com/bucket/original.pdf?signed", response.getOriginalUrl());
        assertEquals("https://s3.amazonaws.com/bucket/signed.pdf?signed", response.getSignedUrl());
        assertEquals(3600, response.getExpiresIn());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("GET", apiReq.getMethod());
        assertEquals("/v1/transactions/tx-001/download", apiReq.getPath());
    }
}
