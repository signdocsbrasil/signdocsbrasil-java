package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Result of cancelling an entire envelope.
 *
 * <p>{@code preservedSignedCount} reports signatures already collected, which
 * are left untouched — cancelling stops the pending signers, it never
 * invalidates evidence already gathered. {@code alreadyCancelled} is set when
 * the envelope was already CANCELLED, in which case {@code cancelledCount} is
 * 0; the endpoint is idempotent, so re-cancelling is a safe no-op.
 */
public class CancelEnvelopeResponse {

    @SerializedName("envelopeId")
    private String envelopeId;

    @SerializedName("status")
    private String status;

    @SerializedName("cancelledCount")
    private int cancelledCount;

    @SerializedName("preservedSignedCount")
    private int preservedSignedCount;

    @SerializedName("cancelledSessions")
    private List<CancelledEnvelopeSession> cancelledSessions;

    @SerializedName("alreadyCancelled")
    private boolean alreadyCancelled;

    public CancelEnvelopeResponse() {
    }

    public String getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(String envelopeId) {
        this.envelopeId = envelopeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCancelledCount() {
        return cancelledCount;
    }

    public void setCancelledCount(int cancelledCount) {
        this.cancelledCount = cancelledCount;
    }

    public int getPreservedSignedCount() {
        return preservedSignedCount;
    }

    public void setPreservedSignedCount(int preservedSignedCount) {
        this.preservedSignedCount = preservedSignedCount;
    }

    public List<CancelledEnvelopeSession> getCancelledSessions() {
        return cancelledSessions;
    }

    public void setCancelledSessions(List<CancelledEnvelopeSession> cancelledSessions) {
        this.cancelledSessions = cancelledSessions;
    }

    public boolean isAlreadyCancelled() {
        return alreadyCancelled;
    }

    public void setAlreadyCancelled(boolean alreadyCancelled) {
        this.alreadyCancelled = alreadyCancelled;
    }

    /** Identifies a session stopped by an envelope cancel. */
    public static class CancelledEnvelopeSession {

        @SerializedName("sessionId")
        private String sessionId;

        @SerializedName("transactionId")
        private String transactionId;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }
    }
}
