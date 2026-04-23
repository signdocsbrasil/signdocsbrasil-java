package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * A signing session returned from the API after creation.
 */
public class SigningSession {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("status")
    private String status;

    @SerializedName("url")
    private String url;

    @SerializedName("clientSecret")
    private String clientSecret;

    @SerializedName("expiresAt")
    private String expiresAt;

    @SerializedName("createdAt")
    private String createdAt;

    /**
     * Set to {@code true} when the server dispatched an invitation email
     * to {@code signer.email} at session creation. Populated only when
     * {@code owner} was provided and {@code signer.email} differs from
     * {@code owner.email}.
     */
    @SerializedName("inviteSent")
    private Boolean inviteSent;

    public SigningSession() {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getInviteSent() {
        return inviteSent;
    }

    public void setInviteSent(Boolean inviteSent) {
        this.inviteSent = inviteSent;
    }

    @Override
    public String toString() {
        return "SigningSession{sessionId='" + sessionId + "', status='" + status + "'}";
    }
}
