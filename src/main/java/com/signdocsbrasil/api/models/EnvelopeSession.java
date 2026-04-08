package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a session within an envelope.
 */
public class EnvelopeSession {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("signerIndex")
    private int signerIndex;

    @SerializedName("status")
    private String status;

    @SerializedName("url")
    private String url;

    @SerializedName("clientSecret")
    private String clientSecret;

    @SerializedName("expiresAt")
    private String expiresAt;

    public EnvelopeSession() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "EnvelopeSession{sessionId='" + sessionId + "', status='" + status + "', signerIndex=" + signerIndex + "}";
    }
}
