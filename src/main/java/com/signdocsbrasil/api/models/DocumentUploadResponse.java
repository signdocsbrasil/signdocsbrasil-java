package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from uploading a document to a transaction.
 */
public class DocumentUploadResponse {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("documentHash")
    private String documentHash;

    @SerializedName("status")
    private String status;

    @SerializedName("uploadedAt")
    private String uploadedAt;

    public DocumentUploadResponse() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
