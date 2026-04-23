package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Lightweight status of a signing session, returned from
 * {@code GET /v1/signing-sessions/{sessionId}/status} for polling.
 */
public class SigningSessionStatus {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("status")
    private String status;

    @SerializedName("completedAt")
    private String completedAt;

    @SerializedName("evidenceId")
    private String evidenceId;

    public SigningSessionStatus() {
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
    }

    @Override
    public String toString() {
        return "SigningSessionStatus{sessionId='" + sessionId + "', status='" + status + "'}";
    }
}
