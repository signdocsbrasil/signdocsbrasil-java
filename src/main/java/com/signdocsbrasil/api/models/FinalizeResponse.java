package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from finalizing a transaction.
 */
public class FinalizeResponse {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("status")
    private String status;

    @SerializedName("evidenceId")
    private String evidenceId;

    @SerializedName("evidenceHash")
    private String evidenceHash;

    @SerializedName("completedAt")
    private String completedAt;

    public FinalizeResponse() {
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

    public String getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
    }

    public String getEvidenceHash() {
        return evidenceHash;
    }

    public void setEvidenceHash(String evidenceHash) {
        this.evidenceHash = evidenceHash;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
