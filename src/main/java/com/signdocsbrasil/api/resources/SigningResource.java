package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.CompleteSigningRequest;
import com.signdocsbrasil.api.models.CompleteSigningResponse;
import com.signdocsbrasil.api.models.PrepareSigningRequest;
import com.signdocsbrasil.api.models.PrepareSigningResponse;

import java.time.Duration;

/**
 * Resource for digital certificate signing operations (A1 flow).
 */
public final class SigningResource {

    private final HttpClient http;

    public SigningResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Prepares a signing operation by submitting the certificate chain.
     * Returns the hash that must be signed by the client's private key.
     *
     * @param transactionId the transaction ID
     * @param request       the prepare request with certificate chain PEMs
     * @return the prepare response with hash to sign
     */
    public PrepareSigningResponse prepare(String transactionId, PrepareSigningRequest request) {
        return http.request("POST",
                "/v1/transactions/" + transactionId + "/signing/prepare",
                request, PrepareSigningResponse.class);
    }

    /**
     * Prepares a signing operation with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param request       the prepare request with certificate chain PEMs
     * @param timeout       the request timeout
     * @return the prepare response with hash to sign
     */
    public PrepareSigningResponse prepare(String transactionId, PrepareSigningRequest request, Duration timeout) {
        return http.request("POST",
                "/v1/transactions/" + transactionId + "/signing/prepare",
                request, PrepareSigningResponse.class, timeout);
    }

    /**
     * Completes a signing operation by submitting the raw signature.
     *
     * @param transactionId the transaction ID
     * @param request       the complete request with signature request ID and raw signature
     * @return the complete response with signing result details
     */
    public CompleteSigningResponse complete(String transactionId, CompleteSigningRequest request) {
        return http.request("POST",
                "/v1/transactions/" + transactionId + "/signing/complete",
                request, CompleteSigningResponse.class);
    }

    /**
     * Completes a signing operation with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param request       the complete request with signature request ID and raw signature
     * @param timeout       the request timeout
     * @return the complete response with signing result details
     */
    public CompleteSigningResponse complete(String transactionId, CompleteSigningRequest request, Duration timeout) {
        return http.request("POST",
                "/v1/transactions/" + transactionId + "/signing/complete",
                request, CompleteSigningResponse.class, timeout);
    }
}
