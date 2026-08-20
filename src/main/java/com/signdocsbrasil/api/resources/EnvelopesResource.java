package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.AddEnvelopeSessionRequest;
import com.signdocsbrasil.api.models.CancelEnvelopeResponse;
import com.signdocsbrasil.api.models.CreateEnvelopeRequest;
import com.signdocsbrasil.api.models.Envelope;
import com.signdocsbrasil.api.models.EnvelopeCombinedStampResponse;
import com.signdocsbrasil.api.models.EnvelopeDetail;
import com.signdocsbrasil.api.models.EnvelopeSession;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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
     * Adds a signing session to an envelope, with an automatically generated
     * idempotency key.
     *
     * <p>The key matters more here than on most calls: this response carries the
     * only copy of {@code clientSecret}, and the client retries 429/500/503 —
     * so an unkeyed retry creates a second signer, charges the quota again and
     * sends a second invitation.
     *
     * @param envelopeId the envelope ID
     * @param request    the add-session request
     * @return the created envelope session
     */
    public EnvelopeSession addSession(String envelopeId, AddEnvelopeSessionRequest request) {
        return addSession(envelopeId, request, (String) null);
    }

    /**
     * Adds a signing session to an envelope with a specified idempotency key.
     *
     * <p>Use a distinct key per signer. The API scopes its idempotency cache by
     * key and resolved path, and every signer on an envelope shares that path,
     * so one key across the loop returns signer 1's response — and signer 1's
     * {@code clientSecret} — for signer 2.
     *
     * @param envelopeId     the envelope ID
     * @param request        the add-session request
     * @param idempotencyKey the idempotency key, or null to auto-generate
     * @return the created envelope session
     */
    public EnvelopeSession addSession(String envelopeId, AddEnvelopeSessionRequest request,
                                      String idempotencyKey) {
        return http.requestWithIdempotency("POST", "/v1/envelopes/" + envelopeId + "/sessions",
                request, EnvelopeSession.class, idempotencyKey);
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
        return http.requestWithIdempotency("POST", "/v1/envelopes/" + envelopeId + "/sessions",
                request, EnvelopeSession.class, null, timeout);
    }

    // ── cancel ──────────────────────────────────────────────────────────

    /**
     * Cancels an entire envelope.
     *
     * <p>Transitions every non-terminal session and its transaction to CANCELLED
     * and marks the envelope CANCELLED, killing the pending signing links.
     * Signatures already collected are preserved and reported as
     * {@code preservedSignedCount}.
     *
     * <p>Prefer this over cancelling each session individually: it is one call,
     * it records the cancellation as a single auditable terminal event, and it
     * is the only way to move the envelope's own status. Cancelling the member
     * sessions one by one leaves the envelope itself ACTIVE.
     *
     * <p>Idempotent: re-cancelling returns {@code cancelledCount} 0 and
     * {@code alreadyCancelled} true.
     *
     * @param envelopeId the envelope ID
     * @param reason     free-text reason recorded in the audit trail; null lets
     *                   the API default it to {@code envelope_cancelled}
     * @return the cancellation result
     */
    public CancelEnvelopeResponse cancel(String envelopeId, String reason) {
        return http.request("POST", "/v1/envelopes/" + envelopeId + "/cancel",
                cancelBody(reason), CancelEnvelopeResponse.class);
    }

    /**
     * Cancels an entire envelope with a per-request timeout.
     *
     * @param envelopeId the envelope ID
     * @param reason     free-text reason recorded in the audit trail; may be null
     * @param timeout    the request timeout
     * @return the cancellation result
     */
    public CancelEnvelopeResponse cancel(String envelopeId, String reason, Duration timeout) {
        return http.request("POST", "/v1/envelopes/" + envelopeId + "/cancel",
                cancelBody(reason), CancelEnvelopeResponse.class, timeout);
    }

    private static Map<String, String> cancelBody(String reason) {
        Map<String, String> body = new HashMap<>();
        if (reason != null && !reason.isEmpty()) {
            body.put("reason", reason);
        }
        return body;
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
