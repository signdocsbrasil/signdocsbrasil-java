package com.signdocsbrasil.api.resources;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.RegisterWebhookRequest;
import com.signdocsbrasil.api.models.RegisterWebhookResponse;
import com.signdocsbrasil.api.models.Webhook;
import com.signdocsbrasil.api.models.WebhookTestResponse;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Resource for webhook management operations.
 */
public final class WebhooksResource {

    private static final Type WEBHOOK_LIST_ENVELOPE_TYPE =
            new TypeToken<WebhookListEnvelope>() {}.getType();

    /**
     * Envelope shape returned by {@code GET /v1/webhooks}:
     * {@code {"webhooks":[...],"count":N}}. Internal — consumers get a
     * {@link List}<{@link Webhook}> via {@link #list()}.
     */
    private static final class WebhookListEnvelope {
        @SerializedName("webhooks")
        List<Webhook> webhooks;
        @SerializedName("count")
        Integer count;
    }

    private final HttpClient http;

    public WebhooksResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Registers a new webhook endpoint.
     * Returns HTTP 201 Created with the webhook details and signing secret.
     *
     * @param request the webhook registration request
     * @return the registration response including the webhook secret
     */
    public RegisterWebhookResponse register(RegisterWebhookRequest request) {
        return http.request("POST", "/v1/webhooks", request, RegisterWebhookResponse.class);
    }

    /**
     * Registers a new webhook endpoint with a per-request timeout.
     *
     * @param request the webhook registration request
     * @param timeout the request timeout
     * @return the registration response including the webhook secret
     */
    public RegisterWebhookResponse register(RegisterWebhookRequest request, Duration timeout) {
        return http.request("POST", "/v1/webhooks", request, RegisterWebhookResponse.class, timeout);
    }

    /**
     * Lists all registered webhooks.
     *
     * @return the list of webhooks
     */
    public List<Webhook> list() {
        WebhookListEnvelope envelope =
                http.request("GET", "/v1/webhooks", null, WEBHOOK_LIST_ENVELOPE_TYPE);
        return envelope == null || envelope.webhooks == null
                ? Collections.emptyList()
                : envelope.webhooks;
    }

    /**
     * Lists all registered webhooks with a per-request timeout.
     *
     * @param timeout the request timeout
     * @return the list of webhooks
     */
    public List<Webhook> list(Duration timeout) {
        WebhookListEnvelope envelope =
                http.request("GET", "/v1/webhooks", null, WEBHOOK_LIST_ENVELOPE_TYPE, timeout);
        return envelope == null || envelope.webhooks == null
                ? Collections.emptyList()
                : envelope.webhooks;
    }

    /**
     * Deletes a webhook by ID. Returns HTTP 204 No Content.
     *
     * @param webhookId the webhook ID to delete
     */
    public void delete(String webhookId) {
        http.request("DELETE", "/v1/webhooks/" + webhookId, null, Void.class);
    }

    /**
     * Deletes a webhook by ID with a per-request timeout.
     *
     * @param webhookId the webhook ID to delete
     * @param timeout   the request timeout
     */
    public void delete(String webhookId, Duration timeout) {
        http.request("DELETE", "/v1/webhooks/" + webhookId, null, Void.class, timeout);
    }

    /**
     * Sends a test delivery to a webhook endpoint.
     *
     * @param webhookId the webhook ID to test
     * @return the test delivery response
     */
    public WebhookTestResponse test(String webhookId) {
        return http.request("POST", "/v1/webhooks/" + webhookId + "/test",
                null, WebhookTestResponse.class);
    }

    /**
     * Sends a test delivery to a webhook endpoint with a per-request timeout.
     *
     * @param webhookId the webhook ID to test
     * @param timeout   the request timeout
     * @return the test delivery response
     */
    public WebhookTestResponse test(String webhookId, Duration timeout) {
        return http.request("POST", "/v1/webhooks/" + webhookId + "/test",
                null, WebhookTestResponse.class, timeout);
    }
}
