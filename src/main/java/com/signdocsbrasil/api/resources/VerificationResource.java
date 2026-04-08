package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
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
}
