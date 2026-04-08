package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response from registering a webhook, including the signing secret.
 */
public class RegisterWebhookResponse {

    @SerializedName("webhookId")
    private String webhookId;

    @SerializedName("url")
    private String url;

    @SerializedName("secret")
    private String secret;

    @SerializedName("events")
    private List<String> events;

    @SerializedName("status")
    private String status;

    @SerializedName("createdAt")
    private String createdAt;

    public RegisterWebhookResponse() {
    }

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
