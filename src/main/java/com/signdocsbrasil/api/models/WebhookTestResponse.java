package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from testing a webhook delivery via
 * {@code POST /v1/webhooks/{webhookId}/test}.
 *
 * <p>Shape: {@code {"webhookId": "wh_...", "testDelivery": { ... }}}.
 */
public class WebhookTestResponse {

    @SerializedName("webhookId")
    private String webhookId;

    @SerializedName("testDelivery")
    private WebhookTestDelivery testDelivery;

    public WebhookTestResponse() {
    }

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    public WebhookTestDelivery getTestDelivery() {
        return testDelivery;
    }

    public void setTestDelivery(WebhookTestDelivery testDelivery) {
        this.testDelivery = testDelivery;
    }
}
