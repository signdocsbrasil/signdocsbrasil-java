package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.EnvelopeVerificationResponse;
import com.signdocsbrasil.api.models.VerificationDownloadsResponse;
import com.signdocsbrasil.api.models.VerificationResponse;

import java.time.Duration;

/**
 * Resource for public verification operations.
 * These endpoints do not require authentication.
 */
public final class VerificationResource {

    private final HttpClient http;

    public VerificationResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Verifies an evidence record by its public ID.
     * This endpoint does not require authentication.
     *
     * @param evidenceId the evidence ID
     * @return the verification response
     */
    public VerificationResponse verify(String evidenceId) {
        return http.requestNoAuth("GET", "/v1/verify/" + evidenceId,
                VerificationResponse.class);
    }

    /**
     * Verifies an evidence record with a per-request timeout.
     * This endpoint does not require authentication.
     *
     * @param evidenceId the evidence ID
     * @param timeout    the request timeout
     * @return the verification response
     */
    public VerificationResponse verify(String evidenceId, Duration timeout) {
        return http.requestNoAuth("GET", "/v1/verify/" + evidenceId,
                VerificationResponse.class, timeout);
    }

    /**
     * Gets download URLs for evidence documents.
     * This endpoint does not require authentication.
     *
     * @param evidenceId the evidence ID
     * @return the verification downloads response
     */
    public VerificationDownloadsResponse downloads(String evidenceId) {
        return http.requestNoAuth("GET", "/v1/verify/" + evidenceId + "/downloads",
                VerificationDownloadsResponse.class);
    }

    /**
     * Gets download URLs for evidence documents with a per-request timeout.
     * This endpoint does not require authentication.
     *
     * @param evidenceId the evidence ID
     * @param timeout    the request timeout
     * @return the verification downloads response
     */
    public VerificationDownloadsResponse downloads(String evidenceId, Duration timeout) {
        return http.requestNoAuth("GET", "/v1/verify/" + evidenceId + "/downloads",
                VerificationDownloadsResponse.class, timeout);
    }

    /**
     * Verifies a multi-signer envelope by its ID. Returns envelope status,
     * the list of signers (each with an {@code evidenceId} for drill-down),
     * and consolidated download URLs. For non-PDF envelopes signed with
     * digital certificates, the consolidated {@code .p7s} containing every
     * signer's {@code SignerInfo} is exposed via
     * {@link EnvelopeVerificationResponse.Downloads#getConsolidatedSignature()}.
     * This endpoint does not require authentication.
     *
     * @param envelopeId the envelope ID
     * @return the envelope verification response
     */
    public EnvelopeVerificationResponse verifyEnvelope(String envelopeId) {
        return http.requestNoAuth("GET", "/v1/verify/envelope/" + envelopeId,
                EnvelopeVerificationResponse.class);
    }

    /**
     * Verifies a multi-signer envelope by its ID with a per-request timeout.
     * This endpoint does not require authentication.
     *
     * @param envelopeId the envelope ID
     * @param timeout    the request timeout
     * @return the envelope verification response
     */
    public EnvelopeVerificationResponse verifyEnvelope(String envelopeId, Duration timeout) {
        return http.requestNoAuth("GET", "/v1/verify/envelope/" + envelopeId,
                EnvelopeVerificationResponse.class, timeout);
    }
}
