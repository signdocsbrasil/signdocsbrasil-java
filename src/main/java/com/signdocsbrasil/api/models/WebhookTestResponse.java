package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from testing a webhook delivery.
 */
public class WebhookTestResponse {

    @SerializedName("deliveryId")
    private String deliveryId;

    @SerializedName("status")
    private String status;

    @SerializedName("statusCode")
    private Integer statusCode;

    public WebhookTestResponse() {
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
