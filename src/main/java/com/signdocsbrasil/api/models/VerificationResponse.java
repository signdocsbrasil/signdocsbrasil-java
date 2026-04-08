package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response from verifying an evidence record.
 */
public class VerificationResponse {

    @SerializedName("evidenceId")
    private String evidenceId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("status")
    private String status;

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("documentHash")
    private String documentHash;

    @SerializedName("evidenceHash")
    private String evidenceHash;

    @SerializedName("policy")
    private Policy policy;

    @SerializedName("signer")
    private VerificationSigner signer;

    @SerializedName("steps")
    private List<VerificationStep> steps;

    @SerializedName("tenantName")
    private String tenantName;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("completedAt")
    private String completedAt;

    public VerificationResponse() {
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public String getEvidenceHash() {
        return evidenceHash;
    }

    public void setEvidenceHash(String evidenceHash) {
        this.evidenceHash = evidenceHash;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public VerificationSigner getSigner() {
        return signer;
    }

    public void setSigner(VerificationSigner signer) {
        this.signer = signer;
    }

    public List<VerificationStep> getSteps() {
        return steps;
    }

    public void setSteps(List<VerificationStep> steps) {
        this.steps = steps;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * Signer information within a verification response.
     */
    public static class VerificationSigner {
        @SerializedName("displayName")
        private String displayName;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
    }

    /**
     * Step information within a verification response.
     */
    public static class VerificationStep {
        @SerializedName("type")
        private String type;

        @SerializedName("status")
        private String status;

        @SerializedName("order")
        private int order;

        @SerializedName("completedAt")
        private String completedAt;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(String completedAt) {
            this.completedAt = completedAt;
        }
    }
}
