package com.signdocsbrasil.api;

import com.signdocsbrasil.api.models.VerifyDocumentRequest;
import com.signdocsbrasil.api.models.VerifyDocumentResponse;
import com.signdocsbrasil.api.resources.VerificationResource;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VerifyDocumentTest {

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

    private VerificationResource createResource() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(baseUrl())
                .maxRetries(0)
                .build();
        AuthHandler auth = new AuthHandler(config);
        HttpClient http = new HttpClient(config, auth);
        return new VerificationResource(http);
    }

    @Test
    void verifyDocumentParsesResponse() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"signed\":true,\"signatureCount\":1,\"signatures\":[" +
                        "{\"method\":\"pkcs7\",\"type\":\"pkcs7\",\"subFilter\":\"adbe.pkcs7.detached\"," +
                        "\"filter\":\"Adobe.PPKLite\",\"confidence\":1.0}]," +
                        "\"checkedAt\":\"2024-11-15T00:05:00.000Z\"}")
                .setHeader("Content-Type", "application/json"));

        VerificationResource resource = createResource();
        VerifyDocumentRequest request =
                new VerifyDocumentRequest("JVBERi0xLjQK...", "contrato-assinado.pdf");
        VerifyDocumentResponse response = resource.verifyDocument(request);

        assertTrue(response.isSigned());
        assertEquals(1, response.getSignatureCount());
        assertNotNull(response.getSignatures());
        assertEquals(1, response.getSignatures().size());
        assertEquals("pkcs7", response.getSignatures().get(0).getMethod());
        assertEquals("pkcs7", response.getSignatures().get(0).getType());
        assertEquals("adbe.pkcs7.detached", response.getSignatures().get(0).getSubFilter());
        assertEquals("Adobe.PPKLite", response.getSignatures().get(0).getFilter());
        assertEquals(1.0, response.getSignatures().get(0).getConfidence());
        assertEquals("2024-11-15T00:05:00.000Z", response.getCheckedAt());
    }

    @Test
    void verifyDocumentIsAuthenticatedPost() throws Exception {
        enqueueToken();
        server.enqueue(new MockResponse()
                .setBody("{\"signed\":false,\"signatureCount\":0,\"signatures\":[]," +
                        "\"checkedAt\":\"2024-11-15T00:05:00.000Z\"}")
                .setHeader("Content-Type", "application/json"));

        VerificationResource resource = createResource();
        VerifyDocumentRequest request = new VerifyDocumentRequest("JVBERi0xLjQK...");
        resource.verifyDocument(request);

        RecordedRequest tokenReq = server.takeRequest(); // token
        assertTrue(tokenReq.getPath().contains("/oauth2/token"));

        RecordedRequest apiReq = server.takeRequest(); // verify document
        assertEquals("POST", apiReq.getMethod());
        assertTrue(apiReq.getPath().endsWith("/v1/verify/document"));
        // Unlike the public verify endpoints, this one is authenticated.
        assertNotNull(apiReq.getHeader("Authorization"));
        assertTrue(apiReq.getHeader("Authorization").startsWith("Bearer "));
        assertTrue(apiReq.getBody().readUtf8().contains("\"content\":\"JVBERi0xLjQK...\""));
    }
}
