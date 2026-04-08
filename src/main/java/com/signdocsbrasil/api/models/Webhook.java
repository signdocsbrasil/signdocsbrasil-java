package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Represents a registered webhook endpoint.
 */
public class Webhook {

    @SerializedName("webhookId")
    private String webhookId;

    @SerializedName("url")
    private String url;

    @SerializedName("events")
    private List<String> events;

    @SerializedName("status")
    private String status;

    @SerializedName("createdAt")
    private String createdAt;

    public Webhook() {
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

    @Override
    public String toString() {
        return "Webhook{webhookId='" + webhookId + "', url='" + url + "', status='" + status + "'}";
    }
}
