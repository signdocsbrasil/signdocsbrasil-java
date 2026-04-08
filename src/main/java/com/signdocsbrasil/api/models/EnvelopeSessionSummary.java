package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Summary of a session within an envelope detail response.
 */
public class EnvelopeSessionSummary {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("signerIndex")
    private int signerIndex;

    @SerializedName("signerName")
    private String signerName;

    @SerializedName("status")
    private String status;

    @SerializedName("completedAt")
    private String completedAt;

    @SerializedName("evidenceId")
    private String evidenceId;

    public EnvelopeSessionSummary() {
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

    public int getSignerIndex() {
        return signerIndex;
    }

    public void setSignerIndex(int signerIndex) {
        this.signerIndex = signerIndex;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
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
        return "EnvelopeSessionSummary{sessionId='" + sessionId + "', status='" + status + "', signerIndex=" + signerIndex + "}";
    }
}
