package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.EnvelopeVerificationResponse;
import com.signdocsbrasil.api.models.VerificationDownloadsResponse;
import com.signdocsbrasil.api.models.VerificationResponse;
import com.signdocsbrasil.api.models.VerifyDocumentRequest;
import com.signdocsbrasil.api.models.VerifyDocumentResponse;

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

    /**
     * Inspects an uploaded PDF for embedded signatures, reporting whether the
     * document is signed, how many signatures were detected, and the type of
     * each ({@code pades}, {@code pkcs7}, {@code legacy}, or
     * {@code digital_certificate}).
     *
     * <p>Unlike the other verification methods on this resource, this endpoint
     * is <strong>authenticated</strong> (Bearer JWT) and requires the
     * {@code verification:write} scope. It is also
     * <strong>production-credentials-only</strong> at runtime — sandbox/HML
     * credentials are rejected by the API.
     *
     * @param request the verify request carrying the base64-encoded PDF
     *                 {@code content} and an optional {@code filename}
     * @return the document verification response
     */
    public VerifyDocumentResponse verifyDocument(VerifyDocumentRequest request) {
        return http.request("POST", "/v1/verify/document", request,
                VerifyDocumentResponse.class);
    }

    /**
     * Inspects an uploaded PDF for embedded signatures with a per-request
     * timeout. Authenticated (Bearer JWT), requires the
     * {@code verification:write} scope, and is production-credentials-only at
     * runtime.
     *
     * @param request the verify request carrying the base64-encoded PDF
     *                 {@code content} and an optional {@code filename}
     * @param timeout the request timeout
     * @return the document verification response
     */
    public VerifyDocumentResponse verifyDocument(VerifyDocumentRequest request, Duration timeout) {
        return http.request("POST", "/v1/verify/document", request,
                VerifyDocumentResponse.class, timeout);
    }
}
