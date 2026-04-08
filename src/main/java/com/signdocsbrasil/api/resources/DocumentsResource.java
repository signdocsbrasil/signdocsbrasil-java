package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.ConfirmDocumentRequest;
import com.signdocsbrasil.api.models.ConfirmDocumentResponse;
import com.signdocsbrasil.api.models.DocumentUploadResponse;
import com.signdocsbrasil.api.models.DownloadResponse;
import com.signdocsbrasil.api.models.PresignRequest;
import com.signdocsbrasil.api.models.PresignResponse;
import com.signdocsbrasil.api.models.UploadDocumentRequest;

import java.time.Duration;

/**
 * Resource for document upload, presign, confirm, and download operations.
 */
public final class DocumentsResource {

    private final HttpClient http;

    public DocumentsResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Uploads a document to a transaction (inline base64 content).
     *
     * @param transactionId the transaction ID
     * @param request       the upload request with document content
     * @return the document upload response
     */
    public DocumentUploadResponse upload(String transactionId, UploadDocumentRequest request) {
        return http.request("POST", "/v1/transactions/" + transactionId + "/document",
                request, DocumentUploadResponse.class);
    }

    /**
     * Uploads a document to a transaction with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param request       the upload request with document content
     * @param timeout       the request timeout
     * @return the document upload response
     */
    public DocumentUploadResponse upload(String transactionId, UploadDocumentRequest request, Duration timeout) {
        return http.request("POST", "/v1/transactions/" + transactionId + "/document",
                request, DocumentUploadResponse.class, timeout);
    }

    /**
     * Gets a presigned URL for direct document upload.
     *
     * @param transactionId the transaction ID
     * @param request       the presign request with content type and filename
     * @return the presign response with upload URL and token
     */
    public PresignResponse presign(String transactionId, PresignRequest request) {
        return http.request("POST", "/v1/transactions/" + transactionId + "/document/presign",
                request, PresignResponse.class);
    }

    /**
     * Gets a presigned URL for direct document upload with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param request       the presign request with content type and filename
     * @param timeout       the request timeout
     * @return the presign response with upload URL and token
     */
    public PresignResponse presign(String transactionId, PresignRequest request, Duration timeout) {
        return http.request("POST", "/v1/transactions/" + transactionId + "/document/presign",
                request, PresignResponse.class, timeout);
    }

    /**
     * Confirms a document upload after using the presigned URL.
     *
     * @param transactionId the transaction ID
     * @param request       the confirm request with upload token
     * @return the confirm document response
     */
    public ConfirmDocumentResponse confirm(String transactionId, ConfirmDocumentRequest request) {
        return http.request("POST", "/v1/transactions/" + transactionId + "/document/confirm",
                request, ConfirmDocumentResponse.class);
    }

    /**
     * Confirms a document upload with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param request       the confirm request with upload token
     * @param timeout       the request timeout
     * @return the confirm document response
     */
    public ConfirmDocumentResponse confirm(String transactionId, ConfirmDocumentRequest request, Duration timeout) {
        return http.request("POST", "/v1/transactions/" + transactionId + "/document/confirm",
                request, ConfirmDocumentResponse.class, timeout);
    }

    /**
     * Gets download URLs for the transaction's document.
     *
     * @param transactionId the transaction ID
     * @return the download response with URLs and metadata
     */
    public DownloadResponse download(String transactionId) {
        return http.request("GET", "/v1/transactions/" + transactionId + "/download",
                null, DownloadResponse.class);
    }

    /**
     * Gets download URLs for the transaction's document with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param timeout       the request timeout
     * @return the download response with URLs and metadata
     */
    public DownloadResponse download(String transactionId, Duration timeout) {
        return http.request("GET", "/v1/transactions/" + transactionId + "/download",
                null, DownloadResponse.class, timeout);
    }
}
