package com.signdocsbrasil.api;

import com.signdocsbrasil.api.models.AdvanceSessionRequest;
import com.signdocsbrasil.api.models.AdvanceSessionResponse;
import com.signdocsbrasil.api.models.SigningSessionBootstrap;
import com.signdocsbrasil.api.resources.SigningSessionsResource;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SigningSessionsNewMethodsTest {

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

    private SigningSessionsResource createResource() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient http = new HttpClient(config, auth);
        return new SigningSessionsResource(http);
    }

    @Test
    void getReturnsBootstrapData() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"transactionId\":\"tx_1\",\"status\":\"ACTIVE\"," +
                        "\"purpose\":\"DOCUMENT_SIGNATURE\",\"signer\":{\"name\":\"Maria\",\"maskedEmail\":\"m***@ex.com\"}," +
                        "\"steps\":[{\"stepId\":\"s1\",\"type\":\"CLICK_ACCEPT\",\"status\":\"PENDING\",\"order\":1}]," +
                        "\"locale\":\"pt-BR\",\"expiresAt\":\"2026-04-10T00:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        SigningSessionBootstrap bootstrap = resource.get("sess_1");

        assertEquals("sess_1", bootstrap.getSessionId());
        assertEquals("tx_1", bootstrap.getTransactionId());
        assertEquals("ACTIVE", bootstrap.getStatus());
        assertEquals("DOCUMENT_SIGNATURE", bootstrap.getPurpose());
        assertEquals("Maria", bootstrap.getSigner().getName());
        assertEquals("m***@ex.com", bootstrap.getSigner().getMaskedEmail());
        assertEquals(1, bootstrap.getSteps().size());
        assertEquals("CLICK_ACCEPT", bootstrap.getSteps().get(0).getType());
        assertEquals("pt-BR", bootstrap.getLocale());

        RecordedRequest req = server.takeRequest(); // token
        req = server.takeRequest(); // get
        assertEquals("GET", req.getMethod());
        assertTrue(req.getPath().endsWith("/v1/signing-sessions/sess_1"));
    }

    @Test
    void advanceSendsRequestBody() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"status\":\"ACTIVE\"," +
                        "\"currentStep\":{\"stepId\":\"s1\",\"type\":\"OTP_CHALLENGE\",\"status\":\"STARTED\"}," +
                        "\"sandbox\":{\"otpCode\":\"123456\"}}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        AdvanceSessionRequest request = new AdvanceSessionRequest("accept");
        AdvanceSessionResponse response = resource.advance("sess_1", request);

        assertEquals("sess_1", response.getSessionId());
        assertEquals("ACTIVE", response.getStatus());
        assertNotNull(response.getCurrentStep());
        assertEquals("OTP_CHALLENGE", response.getCurrentStep().getType());
        assertEquals("STARTED", response.getCurrentStep().getStatus());
        assertNotNull(response.getSandbox());
        assertEquals("123456", response.getSandbox().getOtpCode());

        RecordedRequest req = server.takeRequest(); // token
        req = server.takeRequest(); // advance
        assertEquals("POST", req.getMethod());
        assertTrue(req.getPath().endsWith("/v1/signing-sessions/sess_1/advance"));
        assertTrue(req.getBody().readUtf8().contains("\"action\":\"accept\""));
    }

    @Test
    void advanceWithOtpCode() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"status\":\"ACTIVE\"}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        AdvanceSessionRequest request = new AdvanceSessionRequest("verify_otp");
        request.setOtpCode("123456");
        AdvanceSessionResponse response = resource.advance("sess_1", request);

        assertEquals("sess_1", response.getSessionId());

        RecordedRequest req = server.takeRequest(); // token
        req = server.takeRequest(); // advance
        String body = req.getBody().readUtf8();
        assertTrue(body.contains("\"otpCode\":\"123456\""));
    }

    @Test
    void advanceWithGeolocation() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"status\":\"ACTIVE\"}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        AdvanceSessionRequest request = new AdvanceSessionRequest("accept");
        AdvanceSessionRequest.Geolocation geo = new AdvanceSessionRequest.Geolocation();
        geo.setLatitude(-23.5505);
        geo.setLongitude(-46.6333);
        geo.setAccuracy(10.0);
        geo.setSource("GPS");
        request.setGeolocation(geo);
        resource.advance("sess_1", request);

        RecordedRequest req = server.takeRequest(); // token
        req = server.takeRequest(); // advance
        String body = req.getBody().readUtf8();
        assertTrue(body.contains("\"latitude\":-23.5505"));
        assertTrue(body.contains("\"source\":\"GPS\""));
    }

    @Test
    void advanceCompletedSession() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"status\":\"COMPLETED\"," +
                        "\"evidenceId\":\"ev_1\",\"redirectUrl\":\"https://example.com/done\"," +
                        "\"completedAt\":\"2026-04-08T10:00:00Z\"}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        AdvanceSessionRequest request = new AdvanceSessionRequest("complete_signing");
        request.setSignatureRequestId("sr_1");
        request.setRawSignatureBase64("base64data");
        AdvanceSessionResponse response = resource.advance("sess_1", request);

        assertEquals("COMPLETED", response.getStatus());
        assertEquals("ev_1", response.getEvidenceId());
        assertEquals("https://example.com/done", response.getRedirectUrl());
        assertEquals("2026-04-08T10:00:00Z", response.getCompletedAt());
    }

    @Test
    void advancePrepareSigning() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"status\":\"ACTIVE\"," +
                        "\"signatureRequestId\":\"sr_1\",\"hashToSign\":\"abc123\"," +
                        "\"hashAlgorithm\":\"SHA-256\",\"signatureAlgorithm\":\"SHA256withRSA\"}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        AdvanceSessionRequest request = new AdvanceSessionRequest("prepare_signing");
        request.setCertificateChainPems(List.of("-----BEGIN CERTIFICATE-----\nMIIB...\n-----END CERTIFICATE-----"));
        AdvanceSessionResponse response = resource.advance("sess_1", request);

        assertEquals("sr_1", response.getSignatureRequestId());
        assertEquals("abc123", response.getHashToSign());
        assertEquals("SHA-256", response.getHashAlgorithm());
        assertEquals("SHA256withRSA", response.getSignatureAlgorithm());
    }

    @Test
    void advanceStartLiveness() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"status\":\"ACTIVE\"," +
                        "\"hostedUrl\":\"https://liveness.example.com/session\"," +
                        "\"livenessSessionId\":\"ls_1\"}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        AdvanceSessionRequest request = new AdvanceSessionRequest("start_liveness");
        AdvanceSessionResponse response = resource.advance("sess_1", request);

        assertEquals("https://liveness.example.com/session", response.getHostedUrl());
        assertEquals("ls_1", response.getLivenessSessionId());
    }

    @Test
    void resendOtpReturnsResponse() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_1\",\"status\":\"ACTIVE\"," +
                        "\"currentStep\":{\"stepId\":\"s2\",\"type\":\"OTP_CHALLENGE\"}}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        AdvanceSessionResponse response = resource.resendOtp("sess_1");

        assertEquals("sess_1", response.getSessionId());
        assertEquals("ACTIVE", response.getStatus());
        assertNotNull(response.getCurrentStep());
        assertEquals("OTP_CHALLENGE", response.getCurrentStep().getType());

        RecordedRequest req = server.takeRequest(); // token
        req = server.takeRequest(); // resend
        assertEquals("POST", req.getMethod());
        assertTrue(req.getPath().endsWith("/v1/signing-sessions/sess_1/resend-otp"));
    }

    @Test
    void getBootstrapWithOptionalFields() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_2\",\"transactionId\":\"tx_2\",\"status\":\"ACTIVE\"," +
                        "\"purpose\":\"ACTION_AUTHENTICATION\"," +
                        "\"signer\":{\"name\":\"Jo\\u00e3o\",\"maskedCpf\":\"***.***.***-01\"}," +
                        "\"steps\":[]," +
                        "\"locale\":\"en\",\"expiresAt\":\"2026-04-10T00:00:00Z\"," +
                        "\"action\":{\"type\":\"PIX_TRANSFER\",\"description\":\"Transfer R$ 5000\",\"reference\":\"ref-1\"}," +
                        "\"appearance\":{\"brandColor\":\"#FF0000\",\"companyName\":\"Test Corp\"}," +
                        "\"returnUrl\":\"https://example.com/return\"," +
                        "\"cancelUrl\":\"https://example.com/cancel\"}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        SigningSessionBootstrap bootstrap = resource.get("sess_2");

        assertEquals("ACTION_AUTHENTICATION", bootstrap.getPurpose());
        assertEquals("***.***.***-01", bootstrap.getSigner().getMaskedCpf());
        assertNotNull(bootstrap.getAction());
        assertEquals("PIX_TRANSFER", bootstrap.getAction().getType());
        assertEquals("ref-1", bootstrap.getAction().getReference());
        assertNotNull(bootstrap.getAppearance());
        assertEquals("#FF0000", bootstrap.getAppearance().getBrandColor());
        assertEquals("Test Corp", bootstrap.getAppearance().getCompanyName());
        assertEquals("https://example.com/return", bootstrap.getReturnUrl());
        assertEquals("https://example.com/cancel", bootstrap.getCancelUrl());
    }

    @Test
    void getBootstrapWithDocument() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"sessionId\":\"sess_3\",\"transactionId\":\"tx_3\",\"status\":\"ACTIVE\"," +
                        "\"purpose\":\"DOCUMENT_SIGNATURE\"," +
                        "\"signer\":{\"name\":\"Ana\"}," +
                        "\"steps\":[{\"stepId\":\"s1\",\"type\":\"CLICK_ACCEPT\",\"status\":\"PENDING\",\"order\":1}]," +
                        "\"locale\":\"pt-BR\",\"expiresAt\":\"2026-04-10T00:00:00Z\"," +
                        "\"document\":{\"presignedUrl\":\"https://s3.example.com/doc\",\"filename\":\"contrato.pdf\",\"hash\":\"sha256-abc\"}}")
                .setHeader("Content-Type", "application/json"));

        SigningSessionsResource resource = createResource();
        SigningSessionBootstrap bootstrap = resource.get("sess_3");

        assertNotNull(bootstrap.getDocument());
        assertEquals("https://s3.example.com/doc", bootstrap.getDocument().getPresignedUrl());
        assertEquals("contrato.pdf", bootstrap.getDocument().getFilename());
        assertEquals("sha256-abc", bootstrap.getDocument().getHash());
    }

    @Test
    void advanceResponseToString() {
        AdvanceSessionResponse response = new AdvanceSessionResponse();
        response.setSessionId("sess_1");
        response.setStatus("ACTIVE");
        assertEquals("AdvanceSessionResponse{sessionId='sess_1', status='ACTIVE'}", response.toString());
    }

    @Test
    void bootstrapToString() {
        SigningSessionBootstrap bootstrap = new SigningSessionBootstrap();
        bootstrap.setSessionId("sess_1");
        bootstrap.setStatus("ACTIVE");
        assertEquals("SigningSessionBootstrap{sessionId='sess_1', status='ACTIVE'}", bootstrap.toString());
    }

    @Test
    void advanceRequestGettersSetters() {
        AdvanceSessionRequest req = new AdvanceSessionRequest();
        req.setAction("verify_otp");
        req.setOtpCode("999999");
        req.setLivenessSessionId("ls_1");
        req.setCertificateChainPems(List.of("cert1", "cert2"));
        req.setSignatureRequestId("sr_1");
        req.setRawSignatureBase64("base64");

        assertEquals("verify_otp", req.getAction());
        assertEquals("999999", req.getOtpCode());
        assertEquals("ls_1", req.getLivenessSessionId());
        assertEquals(2, req.getCertificateChainPems().size());
        assertEquals("sr_1", req.getSignatureRequestId());
        assertEquals("base64", req.getRawSignatureBase64());
    }
}
