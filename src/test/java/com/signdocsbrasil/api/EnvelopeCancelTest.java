package com.signdocsbrasil.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.signdocsbrasil.api.models.CancelEnvelopeResponse;
import org.junit.jupiter.api.Test;

/**
 * An envelope is cancelled through its own endpoint. Cancelling the member
 * sessions one by one is not equivalent — it leaves the envelope itself ACTIVE.
 */
class EnvelopeCancelTest {

    private final Gson gson = new Gson();

    @Test
    void deserializesCancelResult() {
        String json = "{\"envelopeId\":\"env_1\",\"status\":\"CANCELLED\","
                + "\"cancelledCount\":2,\"preservedSignedCount\":1,"
                + "\"cancelledSessions\":[{\"sessionId\":\"ss_a\",\"transactionId\":\"tx_a\"}]}";

        CancelEnvelopeResponse resp = gson.fromJson(json, CancelEnvelopeResponse.class);

        assertEquals("env_1", resp.getEnvelopeId());
        assertEquals("CANCELLED", resp.getStatus());
        assertEquals(2, resp.getCancelledCount());
        // A signature already collected is never invalidated by cancelling.
        assertEquals(1, resp.getPreservedSignedCount());
        assertEquals(1, resp.getCancelledSessions().size());
        assertEquals("ss_a", resp.getCancelledSessions().get(0).getSessionId());
        assertFalse(resp.isAlreadyCancelled());
    }

    @Test
    void deserializesIdempotentReCancel() {
        // Re-cancelling is a safe no-op, not an error.
        String json = "{\"envelopeId\":\"env_1\",\"status\":\"CANCELLED\","
                + "\"cancelledCount\":0,\"alreadyCancelled\":true}";

        CancelEnvelopeResponse resp = gson.fromJson(json, CancelEnvelopeResponse.class);

        assertTrue(resp.isAlreadyCancelled());
        assertEquals(0, resp.getCancelledCount());
    }
}
