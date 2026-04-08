package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response containing download URLs for a document.
 */
public class DownloadResponse {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("documentHash")
    private String documentHash;

    @SerializedName("originalUrl")
    private String originalUrl;

    @SerializedName("signedUrl")
    private String signedUrl;

    @SerializedName("expiresIn")
    private int expiresIn;

    public DownloadResponse() {
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

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getSignedUrl() {
        return signedUrl;
    }

    public void setSignedUrl(String signedUrl) {
        this.signedUrl = signedUrl;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
