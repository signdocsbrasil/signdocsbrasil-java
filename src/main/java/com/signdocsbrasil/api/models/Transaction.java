package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 * Represents a signing transaction.
 */
public class Transaction {

    @SerializedName("tenantId")
    private String tenantId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("userExternalId")
    private String userExternalId;

    @SerializedName("documentHash")
    private String documentHash;

    @SerializedName("status")
    private String status;

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("policy")
    private Policy policy;

    @SerializedName("signer")
    private Signer signer;

    @SerializedName("steps")
    private List<Step> steps;

    @SerializedName("documentGroupId")
    private String documentGroupId;

    @SerializedName("signerIndex")
    private Integer signerIndex;

    @SerializedName("totalSigners")
    private Integer totalSigners;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    @SerializedName("expiresAt")
    private String expiresAt;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("submissionDeadline")
    private String submissionDeadline;

    @SerializedName("deadlineStatus")
    private String deadlineStatus;

    public Transaction() {
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserExternalId() {
        return userExternalId;
    }

    public void setUserExternalId(String userExternalId) {
        this.userExternalId = userExternalId;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
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

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Signer getSigner() {
        return signer;
    }

    public void setSigner(Signer signer) {
        this.signer = signer;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getDocumentGroupId() {
        return documentGroupId;
    }

    public void setDocumentGroupId(String documentGroupId) {
        this.documentGroupId = documentGroupId;
    }

    public Integer getSignerIndex() {
        return signerIndex;
    }

    public void setSignerIndex(Integer signerIndex) {
        this.signerIndex = signerIndex;
    }

    public Integer getTotalSigners() {
        return totalSigners;
    }

    public void setTotalSigners(Integer totalSigners) {
        this.totalSigners = totalSigners;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(String submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    public String getDeadlineStatus() {
        return deadlineStatus;
    }

    public void setDeadlineStatus(String deadlineStatus) {
        this.deadlineStatus = deadlineStatus;
    }

    @Override
    public String toString() {
        return "Transaction{transactionId='" + transactionId + "', status='" + status + "', purpose='" + purpose + "'}";
    }
}
