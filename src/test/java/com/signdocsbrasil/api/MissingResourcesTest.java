package com.signdocsbrasil.api;

import com.signdocsbrasil.api.models.*;
import com.signdocsbrasil.api.resources.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 5: Missing resource coverage tests for Java SDK.
 * Covers Steps, Signing, Evidence, DocumentGroups, Users, and Verification
 * resources that were not yet tested.
 */
class MissingResourcesTest {

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

    private SignDocsBrasilClient createClient() {
        String baseUrl = server.url("").toString().replaceAll("/$", "");
        return SignDocsBrasilClient.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl)
                .maxRetries(0)
                .build();
    }

    // ── Steps ─────────────────────────────────────────────────

    @Test
    void stepsListReturnsSteps() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"steps\":[" +
                        "{\"stepId\":\"step_1\",\"type\":\"CLICK_ACCEPT\",\"status\":\"PENDING\",\"order\":1,\"maxAttempts\":3}," +
                        "{\"stepId\":\"step_2\",\"type\":\"OTP_CHALLENGE\",\"status\":\"PENDING\",\"order\":2,\"maxAttempts\":3}" +
                        "]}")
                .setHeader("Content-Type", "application/json"));

        StepListResponse stepList = client.steps().list("tx_1");

        assertNotNull(stepList);
        assertNotNull(stepList.getSteps());
        assertEquals(2, stepList.getSteps().size());
        assertEquals("step_1", stepList.getSteps().get(0).getStepId());
        assertEquals("CLICK_ACCEPT", stepList.getSteps().get(0).getType());
        assertEquals("step_2", stepList.getSteps().get(1).getStepId());
        assertEquals("OTP_CHALLENGE", stepList.getSteps().get(1).getType());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("GET", apiReq.getMethod());
        assertEquals("/v1/transactions/tx_1/steps", apiReq.getPath());
    }

    @Test
    void stepsStartReturnsResponse() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"stepId\":\"step_1\",\"type\":\"CLICK_ACCEPT\",\"status\":\"STARTED\",\"message\":\"Step started successfully\"}")
                .setHeader("Content-Type", "application/json"));

        StartStepResponse resp = client.steps().start("tx_1", "step_1", null);

        assertNotNull(resp);
        assertEquals("step_1", resp.getStepId());
        assertEquals("STARTED", resp.getStatus());
        assertEquals("Step started successfully", resp.getMessage());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertTrue(apiReq.getPath().endsWith("/steps/step_1/start"));
    }

    @Test
    void stepsCompleteClickAccept() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"stepId\":\"step_1\",\"type\":\"CLICK_ACCEPT\",\"status\":\"COMPLETED\",\"attempts\":1,\"result\":{\"click\":{\"accepted\":true,\"textVersion\":\"v1.0\"}}}")
                .setHeader("Content-Type", "application/json"));

        StepCompleteResponse resp = client.steps().complete("tx_1", "step_1", Collections.singletonMap("accepted", true));

        assertNotNull(resp);
        assertEquals("step_1", resp.getStepId());
        assertEquals("COMPLETED", resp.getStatus());
        assertEquals(1, resp.getAttempts());
        assertNotNull(resp.getResult());
        assertNotNull(resp.getResult().getClick());
        assertTrue(resp.getResult().getClick().isAccepted());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertTrue(apiReq.getPath().endsWith("/steps/step_1/complete"));
    }

    @Test
    void stepsCompleteOtp() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"stepId\":\"step_2\",\"type\":\"OTP_VERIFY\",\"status\":\"COMPLETED\",\"attempts\":1,\"result\":{\"otp\":{\"verified\":true,\"channel\":\"email\"}}}")
                .setHeader("Content-Type", "application/json"));

        StepCompleteResponse resp = client.steps().complete("tx_1", "step_2", Collections.singletonMap("code", "123456"));

        assertNotNull(resp);
        assertEquals("step_2", resp.getStepId());
        assertEquals("COMPLETED", resp.getStatus());
        assertNotNull(resp.getResult().getOtp());
        assertTrue(resp.getResult().getOtp().isVerified());
        assertEquals("email", resp.getResult().getOtp().getChannel());
    }

    // ── Signing ───────────────────────────────────────────────

    @Test
    void signingPrepareReturnsHashToSign() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"signatureRequestId\":\"sigreq-001\",\"hashToSign\":\"a1b2c3d4e5f6\",\"hashAlgorithm\":\"SHA-256\",\"signatureAlgorithm\":\"RSASSA-PKCS1-v1_5\"}")
                .setHeader("Content-Type", "application/json"));

        PrepareSigningRequest req = new PrepareSigningRequest();
        req.setCertificateChainPems(List.of("-----BEGIN CERTIFICATE-----\nMIIB...leaf\n-----END CERTIFICATE-----"));

        PrepareSigningResponse resp = client.signing().prepare("tx_1", req);

        assertNotNull(resp);
        assertEquals("sigreq-001", resp.getSignatureRequestId());
        assertEquals("a1b2c3d4e5f6", resp.getHashToSign());
        assertEquals("SHA-256", resp.getHashAlgorithm());
        assertEquals("RSASSA-PKCS1-v1_5", resp.getSignatureAlgorithm());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertTrue(apiReq.getPath().endsWith("/signing/prepare"));
    }

    @Test
    void signingCompleteReturnsDigitalSignatureResult() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"stepId\":\"step-sign-001\",\"status\":\"COMPLETED\",\"result\":{\"digitalSignature\":{" +
                        "\"certificateSubject\":\"CN=JOAO SILVA:12345678901, OU=AC EXAMPLE, O=ICP-Brasil\"," +
                        "\"certificateSerial\":\"1234567890ABCDEF\"," +
                        "\"certificateIssuer\":\"CN=AC EXAMPLE v5, O=ICP-Brasil\"," +
                        "\"algorithm\":\"SHA256withRSA\"," +
                        "\"signedAt\":\"2024-11-15T12:05:00.000Z\"," +
                        "\"signedPdfHash\":\"b2c3d4e5f6\"," +
                        "\"signatureFieldName\":\"SignDocs_1\"" +
                        "}}}")
                .setHeader("Content-Type", "application/json"));

        CompleteSigningRequest req = new CompleteSigningRequest();
        req.setSignatureRequestId("sigreq-001");
        req.setRawSignatureBase64("MEUCIA...base64sig...");

        CompleteSigningResponse resp = client.signing().complete("tx_1", req);

        assertNotNull(resp);
        assertEquals("step-sign-001", resp.getStepId());
        assertEquals("COMPLETED", resp.getStatus());
        assertNotNull(resp.getResult());
        assertNotNull(resp.getResult().getDigitalSignature());
        assertEquals("CN=JOAO SILVA:12345678901, OU=AC EXAMPLE, O=ICP-Brasil",
                resp.getResult().getDigitalSignature().getCertificateSubject());
        assertEquals("SHA256withRSA", resp.getResult().getDigitalSignature().getAlgorithm());
        assertEquals("SignDocs_1", resp.getResult().getDigitalSignature().getSignatureFieldName());
    }

    // ── Evidence ──────────────────────────────────────────────

    @Test
    void evidenceGetReturnsFullEvidence() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"evidenceId\":\"ev_1\",\"transactionId\":\"tx_1\",\"status\":\"COMPLETED\"," +
                        "\"signer\":{\"name\":\"Joao Silva\",\"cpf\":\"12345678901\",\"userExternalId\":\"user-ext-001\"}," +
                        "\"steps\":[{\"type\":\"CLICK_ACCEPT\",\"status\":\"COMPLETED\",\"completedAt\":\"2024-11-15T00:01:00.000Z\"}]," +
                        "\"document\":{\"hash\":\"a1b2c3d4\",\"filename\":\"contract.pdf\"}," +
                        "\"createdAt\":\"2024-11-15T00:00:00.000Z\"," +
                        "\"completedAt\":\"2024-11-15T00:01:00.000Z\"}")
                .setHeader("Content-Type", "application/json"));

        Evidence ev = client.evidence().get("tx_1");

        assertNotNull(ev);
        assertEquals("ev_1", ev.getEvidenceId());
        assertEquals("COMPLETED", ev.getStatus());
        assertNotNull(ev.getSigner());
        assertEquals("Joao Silva", ev.getSigner().getName());
        assertEquals("12345678901", ev.getSigner().getCpf());
        assertNotNull(ev.getSteps());
        assertEquals(1, ev.getSteps().size());
        assertEquals("CLICK_ACCEPT", ev.getSteps().get(0).getType());
        assertNotNull(ev.getDocument());
        assertEquals("contract.pdf", ev.getDocument().getFilename());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("GET", apiReq.getMethod());
        assertEquals("/v1/transactions/tx_1/evidence", apiReq.getPath());
    }

    // ── Document Groups ───────────────────────────────────────

    @Test
    void documentGroupsCombinedStampReturnsUrl() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"groupId\":\"grp_1\",\"signerCount\":3,\"downloadUrl\":\"https://s3.example.com/stamped.pdf\",\"expiresIn\":3600}")
                .setHeader("Content-Type", "application/json"));

        CombinedStampResponse resp = client.documentGroups().combinedStamp("grp_1");

        assertNotNull(resp);
        assertEquals("grp_1", resp.getGroupId());
        assertEquals("https://s3.example.com/stamped.pdf", resp.getDownloadUrl());
        assertEquals(3600, resp.getExpiresIn());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("POST", apiReq.getMethod());
        assertEquals("/v1/document-groups/grp_1/combined-stamp", apiReq.getPath());
    }

    // ── Users ─────────────────────────────────────────────────

    @Test
    void usersEnrollSendsPutWithImage() throws Exception {
        SignDocsBrasilClient client = createClient();
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"userExternalId\":\"usr_1\",\"enrollmentHash\":\"abc123\",\"enrollmentVersion\":1,\"enrollmentSource\":\"BANK_PROVIDED\",\"enrolledAt\":\"2024-11-15T00:00:00.000Z\",\"cpf\":\"12345678901\"}")
                .setHeader("Content-Type", "application/json"));

        EnrollUserRequest req = new EnrollUserRequest("base64-jpeg-data", "12345678901");

        EnrollUserResponse resp = client.users().enroll("usr_1", req);

        assertNotNull(resp);
        assertEquals("usr_1", resp.getUserExternalId());
        assertEquals("abc123", resp.getEnrollmentHash());
        assertNotNull(resp.getEnrolledAt());

        server.takeRequest(); // token
        RecordedRequest apiReq = server.takeRequest();
        assertEquals("PUT", apiReq.getMethod());
        assertTrue(apiReq.getPath().contains("/users/usr_1/enrollment"));
    }

    // ── Verification ──────────────────────────────────────────

    @Test
    void verificationVerifyNoAuthRequired() throws Exception {
        SignDocsBrasilClient client = createClient();
        // No token enqueue — verification is public
        server.enqueue(new MockResponse()
                .setBody("{\"evidenceId\":\"ev_1\",\"status\":\"COMPLETED\",\"transactionId\":\"tx_1\"," +
                        "\"signer\":{\"displayName\":\"Joao\"}," +
                        "\"completedAt\":\"2024-11-15T00:01:00.000Z\"}")
                .setHeader("Content-Type", "application/json"));

        VerificationResponse resp = client.verification().verify("ev_1");

        assertNotNull(resp);
        assertEquals("ev_1", resp.getEvidenceId());
        assertEquals("COMPLETED", resp.getStatus());
        assertEquals("tx_1", resp.getTransactionId());

        RecordedRequest apiReq = server.takeRequest();
        assertNull(apiReq.getHeader("Authorization"));
        assertEquals("GET", apiReq.getMethod());
        assertEquals("/v1/verify/ev_1", apiReq.getPath());
    }

    @Test
    void verificationDownloadsNoAuthRequired() throws Exception {
        SignDocsBrasilClient client = createClient();
        server.enqueue(new MockResponse()
                .setBody("{\"evidenceId\":\"ev_1\"," +
                        "\"downloads\":{" +
                        "\"originalDocument\":{\"url\":\"https://s3/orig.pdf\",\"filename\":\"contrato.pdf\"}," +
                        "\"evidencePack\":{\"url\":\"https://s3/report.pdf\",\"filename\":\"evidence.p7m\"}," +
                        "\"finalPdf\":{\"url\":\"https://s3/final.pdf\",\"filename\":\"final.pdf\"}," +
                        "\"signedSignature\":{\"url\":\"https://s3/sig.p7s\",\"filename\":\"signature.p7s\"}" +
                        "}}")
                .setHeader("Content-Type", "application/json"));

        VerificationDownloadsResponse resp = client.verification().downloads("ev_1");

        assertNotNull(resp);
        assertEquals("ev_1", resp.getEvidenceId());
        assertNotNull(resp.getDownloads());
        assertEquals("https://s3/orig.pdf", resp.getDownloads().getOriginalDocument().getUrl());
        assertEquals("https://s3/report.pdf", resp.getDownloads().getEvidencePack().getUrl());
        assertEquals("https://s3/final.pdf", resp.getDownloads().getFinalPdf().getUrl());
        assertEquals("https://s3/sig.p7s", resp.getDownloads().getSignedSignature().getUrl());

        RecordedRequest apiReq = server.takeRequest();
        assertNull(apiReq.getHeader("Authorization"));
    }

    @Test
    void verificationVerifyEnvelopeNoAuthRequired() throws Exception {
        SignDocsBrasilClient client = createClient();
        server.enqueue(new MockResponse()
                .setBody("{" +
                        "\"envelopeId\":\"env_1\"," +
                        "\"status\":\"COMPLETED\"," +
                        "\"signingMode\":\"SEQUENTIAL\"," +
                        "\"totalSigners\":2," +
                        "\"completedSessions\":2," +
                        "\"documentHash\":\"sha256:abc\"," +
                        "\"tenantName\":\"Acme\"," +
                        "\"tenantCnpj\":\"12345678000100\"," +
                        "\"signers\":[" +
                        "{\"signerIndex\":1,\"displayName\":\"João Silva\",\"cpfCnpj\":\"12345678901\",\"status\":\"COMPLETED\",\"evidenceId\":\"ev_a\",\"completedAt\":\"2026-04-13T18:00:00Z\"}," +
                        "{\"signerIndex\":2,\"displayName\":\"Maria Souza\",\"status\":\"COMPLETED\",\"evidenceId\":\"ev_b\",\"completedAt\":\"2026-04-13T18:30:00Z\"}" +
                        "]," +
                        "\"downloads\":{" +
                        "\"consolidatedSignature\":{\"url\":\"https://s3/envelope.p7s\",\"filename\":\"signature.p7s\"}" +
                        "}," +
                        "\"createdAt\":\"2026-04-13T17:00:00Z\"," +
                        "\"completedAt\":\"2026-04-13T18:30:00Z\"" +
                        "}")
                .setHeader("Content-Type", "application/json"));

        EnvelopeVerificationResponse resp = client.verification().verifyEnvelope("env_1");

        assertNotNull(resp);
        assertEquals("env_1", resp.getEnvelopeId());
        assertEquals("SEQUENTIAL", resp.getSigningMode());
        assertEquals(2, resp.getTotalSigners());
        assertEquals(2, resp.getSigners().size());
        assertEquals("João Silva", resp.getSigners().get(0).getDisplayName());
        assertEquals("12345678901", resp.getSigners().get(0).getCpfCnpj());
        assertEquals("ev_b", resp.getSigners().get(1).getEvidenceId());
        assertNotNull(resp.getDownloads());
        assertNotNull(resp.getDownloads().getConsolidatedSignature());
        assertEquals("signature.p7s", resp.getDownloads().getConsolidatedSignature().getFilename());
        assertNull(resp.getDownloads().getCombinedSignedPdf());

        RecordedRequest apiReq = server.takeRequest();
        assertNull(apiReq.getHeader("Authorization"));
        assertEquals("GET", apiReq.getMethod());
        assertEquals("/v1/verify/envelope/env_1", apiReq.getPath());
    }
}
