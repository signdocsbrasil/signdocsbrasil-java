package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from cancelling a signing session.
 */
public class CancelSigningSessionResponse {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("status")
    private String status;

    @SerializedName("cancelledAt")
    private String cancelledAt;

    public CancelSigningSessionResponse() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(String cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
