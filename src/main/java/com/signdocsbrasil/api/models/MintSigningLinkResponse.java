package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * A freshly minted signing URL for an existing session.
 */
public class MintSigningLinkResponse {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("url")
    private String url;

    @SerializedName("expiresAt")
    private String expiresAt;

    @SerializedName("expiresIn")
    private int expiresIn;

    public MintSigningLinkResponse() {
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

    /**
     * The single-use signing URL. Treat it as a bearer credential.
     *
     * @return the signing URL
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The deadline of the original session. Minting a link does not extend it.
     *
     * @return the ISO 8601 expiry timestamp
     */
    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Seconds remaining until {@link #getExpiresAt()}.
     *
     * @return the remaining lifetime in seconds
     */
    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
