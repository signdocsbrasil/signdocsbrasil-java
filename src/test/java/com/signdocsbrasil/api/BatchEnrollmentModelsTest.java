package com.signdocsbrasil.api;

import com.google.gson.Gson;
import com.signdocsbrasil.api.models.BatchEnrollmentModels;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BatchEnrollmentModelsTest {

    private final Gson gson = new Gson();

    @Test
    void serialisesARequestWithTheWireFieldNames() {
        BatchEnrollmentModels.Item item =
                new BatchEnrollmentModels.Item("matricula-4471", "aW1n", "11144477735");
        item.setSource("BANK_PROVIDED");

        String json = gson.toJson(new BatchEnrollmentModels.Request(List.of(item)));

        assertTrue(json.contains("\"userExternalId\":\"matricula-4471\""));
        assertTrue(json.contains("\"cpf\":\"11144477735\""));
        assertTrue(json.contains("\"source\":\"BANK_PROVIDED\""));
    }

    @Test
    void omitsDryRunWhenUnset() {
        String json = gson.toJson(new BatchEnrollmentModels.Request(
                List.of(new BatchEnrollmentModels.Item("a", "aW1n", "11144477735"))));

        assertTrue(!json.contains("dryRun"));
    }

    @Test
    void carriesDryRunWhenSet() {
        BatchEnrollmentModels.Request req = new BatchEnrollmentModels.Request();
        req.setEnrollments(List.of(new BatchEnrollmentModels.Item("a", "aW1n", "11144477735")));
        req.setDryRun(true);

        assertTrue(gson.toJson(req).contains("\"dryRun\":true"));
        assertEquals(Boolean.TRUE, req.getDryRun());
        assertEquals(1, req.getEnrollments().size());
    }

    @Test
    void itemAccessorsRoundTrip() {
        BatchEnrollmentModels.Item item = new BatchEnrollmentModels.Item();
        item.setUserExternalId("matricula-9");
        item.setImage("aW1n");
        item.setCpf("11144477735");
        item.setSource("DOCUMENT_PHOTO");

        assertEquals("matricula-9", item.getUserExternalId());
        assertEquals("aW1n", item.getImage());
        assertEquals("11144477735", item.getCpf());
        assertEquals("DOCUMENT_PHOTO", item.getSource());
    }

    @Test
    void parsesADryRunResponseIncludingQualityMetrics() {
        String json = "{ \"dryRun\": true, \"submitted\": 3, \"usable\": 1, \"marginal\": 1, \"rejected\": 1,"
                + "\"results\": ["
                + "{ \"index\": 0, \"userExternalId\": \"a\", \"status\": \"usable\","
                + "\"faceConfidence\": 99.99,"
                + "\"quality\": { \"brightness\": 72.0, \"sharpness\": 92.0 },"
                + "\"pose\": { \"yaw\": -3.0, \"pitch\": 2.0, \"roll\": 1.0 },"
                + "\"faceCoverage\": 0.4318, \"warnings\": [] },"
                + "{ \"index\": 1, \"userExternalId\": \"b\", \"status\": \"marginal\","
                + "\"warnings\": [\"LOW_BRIGHTNESS\", \"LOW_SHARPNESS\"] },"
                + "{ \"index\": 2, \"userExternalId\": \"c\", \"status\": \"rejected\","
                + "\"error\": \"No face detected in image. Provide a clear facial photo.\" }"
                + "] }";

        BatchEnrollmentModels.Response res = gson.fromJson(json, BatchEnrollmentModels.Response.class);

        assertEquals(Boolean.TRUE, res.getDryRun());
        assertEquals(3, res.getSubmitted());
        assertEquals(1, res.getUsable());
        assertEquals(1, res.getMarginal());
        assertEquals(1, res.getRejected());
        assertNull(res.getSucceeded());

        BatchEnrollmentModels.Result good = res.getResults().get(0);
        assertEquals(0, good.getIndex());
        assertEquals("a", good.getUserExternalId());
        assertEquals("usable", good.getStatus());
        assertEquals(99.99, good.getFaceConfidence(), 0.001);
        assertEquals(72.0, good.getQuality().getBrightness(), 0.001);
        assertEquals(92.0, good.getQuality().getSharpness(), 0.001);
        assertEquals(-3.0, good.getPose().getYaw(), 0.001);
        assertEquals(2.0, good.getPose().getPitch(), 0.001);
        assertEquals(1.0, good.getPose().getRoll(), 0.001);
        assertEquals(0.4318, good.getFaceCoverage(), 0.0001);
        assertTrue(good.getWarnings().isEmpty());

        // The row that matters: it would enrol without complaint today and is
        // exactly what becomes a rejected signature later.
        BatchEnrollmentModels.Result weak = res.getResults().get(1);
        assertEquals("marginal", weak.getStatus());
        assertTrue(weak.getWarnings().contains(BatchEnrollmentModels.WARNING_LOW_BRIGHTNESS));
        assertTrue(weak.getWarnings().contains(BatchEnrollmentModels.WARNING_LOW_SHARPNESS));
        assertNull(weak.getQuality());

        assertNotNull(res.getResults().get(2).getError());
    }

    @Test
    void parsesARealWriteResponseWithoutDryRunFields() {
        String json = "{ \"submitted\": 2, \"succeeded\": 2, \"failed\": 0,"
                + "\"results\": ["
                + "{ \"index\": 0, \"userExternalId\": \"a\", \"status\": \"enrolled\","
                + "\"enrollmentVersion\": 3, \"expiresAt\": \"2026-11-30T00:00:00.000Z\" },"
                + "{ \"index\": 1, \"userExternalId\": \"b\", \"status\": \"enrolled\", \"enrollmentVersion\": 1 }"
                + "] }";

        BatchEnrollmentModels.Response res = gson.fromJson(json, BatchEnrollmentModels.Response.class);

        assertNull(res.getDryRun());
        assertNull(res.getUsable());
        assertEquals(2, res.getSucceeded());
        assertEquals(0, res.getFailed());
        assertEquals(3, res.getResults().get(0).getEnrollmentVersion());
        assertEquals("2026-11-30T00:00:00.000Z", res.getResults().get(0).getExpiresAt());
        assertNull(res.getResults().get(0).getFaceCoverage());
    }

    @Test
    void exposesTheWarningCodesAsConstants() {
        assertEquals("LOW_BRIGHTNESS", BatchEnrollmentModels.WARNING_LOW_BRIGHTNESS);
        assertEquals("LOW_SHARPNESS", BatchEnrollmentModels.WARNING_LOW_SHARPNESS);
        assertEquals("FACE_TOO_SMALL", BatchEnrollmentModels.WARNING_FACE_TOO_SMALL);
        assertEquals("HEAD_TURNED", BatchEnrollmentModels.WARNING_HEAD_TURNED);
    }
}
