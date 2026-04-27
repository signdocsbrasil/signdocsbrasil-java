package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Nested delivery result returned inside {@link WebhookTestResponse}.
 *
 * <p>Mirrors the {@code testDelivery} object in the API response for
 * {@code POST /v1/webhooks/{webhookId}/test}.
 */
public class WebhookTestDelivery {

    @SerializedName("httpStatus")
    private int httpStatus;

    @SerializedName("success")
    private boolean success;

    @SerializedName("error")
    private String error;

    @SerializedName("timestamp")
    private String timestamp;

    public WebhookTestDelivery() {
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return optional error message when {@link #isSuccess()} is {@code false},
     *         or {@code null} when the delivery succeeded.
     */
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
