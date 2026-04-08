package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Request to register a new webhook endpoint.
 */
public class RegisterWebhookRequest {

    @SerializedName("url")
    private String url;

    @SerializedName("events")
    private List<String> events;

    public RegisterWebhookRequest() {
    }

    public RegisterWebhookRequest(String url, List<String> events) {
        this.url = url;
        this.events = events;
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
}
