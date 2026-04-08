package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.Evidence;

import java.time.Duration;

/**
 * Resource for evidence retrieval operations.
 */
public final class EvidenceResource {

    private final HttpClient http;

    public EvidenceResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Retrieves the evidence record for a completed transaction.
     *
     * @param transactionId the transaction ID
     * @return the evidence record
     */
    public Evidence get(String transactionId) {
        return http.request("GET", "/v1/transactions/" + transactionId + "/evidence",
                null, Evidence.class);
    }

    /**
     * Retrieves the evidence record for a completed transaction with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param timeout       the request timeout
     * @return the evidence record
     */
    public Evidence get(String transactionId, Duration timeout) {
        return http.request("GET", "/v1/transactions/" + transactionId + "/evidence",
                null, Evidence.class, timeout);
    }
}
