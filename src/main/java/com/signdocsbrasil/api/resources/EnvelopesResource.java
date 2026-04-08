package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.AddEnvelopeSessionRequest;
import com.signdocsbrasil.api.models.CreateEnvelopeRequest;
import com.signdocsbrasil.api.models.Envelope;
import com.signdocsbrasil.api.models.EnvelopeCombinedStampResponse;
import com.signdocsbrasil.api.models.EnvelopeDetail;
import com.signdocsbrasil.api.models.EnvelopeSession;

import java.time.Duration;

/**
 * Resource for envelope operations.
 */
public final class EnvelopesResource {

    private final HttpClient http;

    public EnvelopesResource(HttpClient http) {
        this.http = http;
    }

    // ── create ──────────────────────────────────────────────────────────

    /**
     * Creates a new envelope with an automatically generated idempotency key.
     *
     * @param request the envelope creation request
     * @return the created envelope
     */
    public Envelope create(CreateEnvelopeRequest request) {
        return create(request, (String) null);
    }

    /**
     * Creates a new envelope with a specified idempotency key.
     *
     * @param request        the envelope creation request
     * @param idempotencyKey the idempotency key, or null to auto-generate
     * @return the created envelope
     */
    public Envelope create(CreateEnvelopeRequest request, String idempotencyKey) {
        return http.requestWithIdempotency("POST", "/v1/envelopes", request,
                Envelope.class, idempotencyKey);
    }

    /**
     * Creates a new envelope with a per-request timeout.
     *
     * @param request the envelope creation request
     * @param timeout the request timeout
     * @return the created envelope
     */
    public Envelope create(CreateEnvelopeRequest request, Duration timeout) {
        return http.requestWithIdempotency("POST", "/v1/envelopes", request,
                Envelope.class, null, timeout);
    }

    /**
     * Creates a new envelope with a specified idempotency key and per-request timeout.
     *
     * @param request        the envelope creation request
     * @param idempotencyKey the idempotency key, or null to auto-generate
     * @param timeout        the request timeout
     * @return the created envelope
     */
    public Envelope create(CreateEnvelopeRequest request, String idempotencyKey, Duration timeout) {
        return http.requestWithIdempotency("POST", "/v1/envelopes", request,
                Envelope.class, idempotencyKey, timeout);
    }

    // ── get ─────────────────────────────────────────────────────────────

    /**
     * Retrieves an envelope by ID.
     *
     * @param envelopeId the envelope ID
     * @return the envelope detail
     */
    public EnvelopeDetail get(String envelopeId) {
        return http.request("GET", "/v1/envelopes/" + envelopeId,
                null, EnvelopeDetail.class);
    }

    /**
     * Retrieves an envelope by ID with a per-request timeout.
     *
     * @param envelopeId the envelope ID
     * @param timeout    the request timeout
     * @return the envelope detail
     */
    public EnvelopeDetail get(String envelopeId, Duration timeout) {
        return http.request("GET", "/v1/envelopes/" + envelopeId,
                null, EnvelopeDetail.class, timeout);
    }

    // ── addSession ──────────────────────────────────────────────────────

    /**
     * Adds a signing session to an envelope.
     *
     * @param envelopeId the envelope ID
     * @param request    the add-session request
     * @return the created envelope session
     */
    public EnvelopeSession addSession(String envelopeId, AddEnvelopeSessionRequest request) {
        return http.request("POST", "/v1/envelopes/" + envelopeId + "/sessions",
                request, EnvelopeSession.class);
    }

    /**
     * Adds a signing session to an envelope with a per-request timeout.
     *
     * @param envelopeId the envelope ID
     * @param request    the add-session request
     * @param timeout    the request timeout
     * @return the created envelope session
     */
    public EnvelopeSession addSession(String envelopeId, AddEnvelopeSessionRequest request, Duration timeout) {
        return http.request("POST", "/v1/envelopes/" + envelopeId + "/sessions",
                request, EnvelopeSession.class, timeout);
    }

    // ── combinedStamp ───────────────────────────────────────────────────

    /**
     * Requests a combined stamp for a completed envelope.
     *
     * @param envelopeId the envelope ID
     * @return the combined stamp response with download URL
     */
    public EnvelopeCombinedStampResponse combinedStamp(String envelopeId) {
        return http.request("POST", "/v1/envelopes/" + envelopeId + "/combined-stamp",
                null, EnvelopeCombinedStampResponse.class);
    }

    /**
     * Requests a combined stamp for a completed envelope with a per-request timeout.
     *
     * @param envelopeId the envelope ID
     * @param timeout    the request timeout
     * @return the combined stamp response with download URL
     */
    public EnvelopeCombinedStampResponse combinedStamp(String envelopeId, Duration timeout) {
        return http.request("POST", "/v1/envelopes/" + envelopeId + "/combined-stamp",
                null, EnvelopeCombinedStampResponse.class, timeout);
    }
}
