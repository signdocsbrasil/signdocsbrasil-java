package com.signdocsbrasil.api;

import com.signdocsbrasil.api.models.BatchEnrollmentModels;
import com.signdocsbrasil.api.models.DeleteEnrollmentResponse;
import com.signdocsbrasil.api.models.EnrollmentStatusResponse;
import com.signdocsbrasil.api.resources.UsersResource;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsersResourceTest {

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

    private UsersResource resource() {
        Config config = Config.builder()
                .clientId("test-client")
                .clientSecret("test-secret")
                .baseUrl(server.url("").toString().replaceAll("/$", ""))
                .maxRetries(0)
                .build();
        return new UsersResource(new HttpClient(config, new AuthHandler(config)));
    }

    private void enqueueToken() {
        server.enqueue(new MockResponse()
                .setBody("{\"access_token\":\"tok\",\"expires_in\":3600}")
                .setHeader("Content-Type", "application/json"));
    }

    private void enqueueJson(String body) {
        server.enqueue(new MockResponse().setBody(body).setHeader("Content-Type", "application/json"));
    }

    @Test
    void readsEnrollmentStatus() throws Exception {
        enqueueToken();
        enqueueJson("{\"userExternalId\":\"a\",\"enrollmentSource\":\"BANK_PROVIDED\","
                + "\"enrollmentVersion\":2,\"enrollmentHash\":\"h\","
                + "\"enrolledAt\":\"2026-09-01T00:00:00.000Z\","
                + "\"expiresAt\":\"2026-11-30T00:00:00.000Z\",\"expired\":false,"
                + "\"retentionDays\":90,\"maskedCpf\":\"***7735\"}");

        EnrollmentStatusResponse res = resource().getEnrollment("a");

        server.takeRequest();
        RecordedRequest req = server.takeRequest();
        assertEquals("GET", req.getMethod());
        assertEquals("/v1/users/a/enrollment", req.getPath());
        assertEquals("2026-11-30T00:00:00.000Z", res.getExpiresAt());
        assertEquals(Boolean.FALSE, res.getExpired());
        assertEquals(90, res.getRetentionDays());
    }

    @Test
    void readsEnrollmentStatusWithTimeout() throws Exception {
        enqueueToken();
        enqueueJson("{\"userExternalId\":\"a\",\"expired\":true}");

        assertEquals(Boolean.TRUE, resource().getEnrollment("a", Duration.ofSeconds(5)).getExpired());
    }

    @Test
    void deletesAnEnrollmentAndReportsDestroyedVersions() throws Exception {
        enqueueToken();
        enqueueJson("{\"userExternalId\":\"a\",\"deleted\":true,"
                + "\"deletedAt\":\"2026-09-01T21:00:40.260Z\",\"versionsDeleted\":3}");

        DeleteEnrollmentResponse res = resource().deleteEnrollment("a");

        server.takeRequest();
        assertEquals("DELETE", server.takeRequest().getMethod());
        assertEquals(Boolean.TRUE, res.getDeleted());
        // Versioned storage: a plain delete would only leave a marker, so the
        // count is the evidence the image is actually gone.
        assertEquals(3, res.getVersionsDeleted());
    }

    @Test
    void deletesAnEnrollmentWithTimeout() throws Exception {
        enqueueToken();
        enqueueJson("{\"userExternalId\":\"a\",\"deleted\":true,\"deletedAt\":\"x\"}");

        assertEquals(Boolean.TRUE, resource().deleteEnrollment("a", Duration.ofSeconds(5)).getDeleted());
    }

    @Test
    void postsABatchToTheCollectionRoute() throws Exception {
        enqueueToken();
        enqueueJson("{\"submitted\":1,\"succeeded\":1,\"failed\":0,"
                + "\"results\":[{\"index\":0,\"userExternalId\":\"a\",\"status\":\"enrolled\"}]}");

        BatchEnrollmentModels.Response res = resource().enrollBatch(
                new BatchEnrollmentModels.Request(Collections.singletonList(
                        new BatchEnrollmentModels.Item("a", "aW1n", "11144477735"))));

        server.takeRequest();
        RecordedRequest req = server.takeRequest();
        assertEquals("POST", req.getMethod());
        assertEquals("/v1/users/enrollments", req.getPath());
        assertEquals(1, res.getSucceeded());
        assertNull(res.getDryRun());
    }

    @Test
    void sendsDryRunAndParsesTheQualityVerdicts() throws Exception {
        enqueueToken();
        enqueueJson("{\"dryRun\":true,\"submitted\":2,\"usable\":1,\"marginal\":1,\"rejected\":0,"
                + "\"results\":["
                + "{\"index\":0,\"status\":\"usable\",\"warnings\":[]},"
                + "{\"index\":1,\"status\":\"marginal\",\"warnings\":[\"LOW_BRIGHTNESS\"]}]}");

        BatchEnrollmentModels.Request request = new BatchEnrollmentModels.Request(
                Collections.singletonList(new BatchEnrollmentModels.Item("a", "aW1n", "11144477735")));
        request.setDryRun(true);

        BatchEnrollmentModels.Response res = resource().enrollBatch(request, Duration.ofSeconds(10));

        server.takeRequest();
        RecordedRequest req = server.takeRequest();
        assertTrue(req.getBody().readUtf8().contains("\"dryRun\":true"));
        assertEquals(Boolean.TRUE, res.getDryRun());
        assertEquals(1, res.getMarginal());
        assertTrue(res.getResults().get(1).getWarnings()
                .contains(BatchEnrollmentModels.WARNING_LOW_BRIGHTNESS));
    }
}
