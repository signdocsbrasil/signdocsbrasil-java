package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from confirming a document upload.
 */
public class ConfirmDocumentResponse {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("status")
    private String status;

    @SerializedName("documentHash")
    private String documentHash;

    public ConfirmDocumentResponse() {
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

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }
}
