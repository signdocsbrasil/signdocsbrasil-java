package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request to verify a PDF document's embedded signatures, sent to
 * {@code POST /v1/verify/document}.
 *
 * <p>The document content must be supplied as a base64-encoded PDF in
 * {@link #content}. An optional {@link #filename} may be provided for logging
 * and diagnostics.
 */
public class VerifyDocumentRequest {

    @SerializedName("content")
    private String content;

    @SerializedName("filename")
    private String filename;

    public VerifyDocumentRequest() {
    }

    public VerifyDocumentRequest(String content) {
        this.content = content;
    }

    public VerifyDocumentRequest(String content, String filename) {
        this.content = content;
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
