package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Request to create a new envelope.
 */
public class CreateEnvelopeRequest {

    @SerializedName("signingMode")
    private String signingMode;

    @SerializedName("totalSigners")
    private int totalSigners;

    @SerializedName("document")
    private EnvelopeDocument document;

    @SerializedName("returnUrl")
    private String returnUrl;

    @SerializedName("cancelUrl")
    private String cancelUrl;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    @SerializedName("locale")
    private String locale;

    @SerializedName("expiresInMinutes")
    private Integer expiresInMinutes;

    public CreateEnvelopeRequest() {
    }

    public String getSigningMode() {
        return signingMode;
    }

    public void setSigningMode(String signingMode) {
        this.signingMode = signingMode;
    }

    public int getTotalSigners() {
        return totalSigners;
    }

    public void setTotalSigners(int totalSigners) {
        this.totalSigners = totalSigners;
    }

    /**
     * Sets the document content and filename via the nested document object.
     *
     * @param content  the base64-encoded document content
     * @param filename the document filename, or null
     */
    public void setDocument(String content, String filename) {
        this.document = new EnvelopeDocument(content, filename);
    }

    /**
     * Returns the base64-encoded document content.
     */
    public String getDocumentContent() {
        return document != null ? document.content : null;
    }

    /**
     * Returns the document filename.
     */
    public String getDocumentFilename() {
        return document != null ? document.filename : null;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getExpiresInMinutes() {
        return expiresInMinutes;
    }

    public void setExpiresInMinutes(Integer expiresInMinutes) {
        this.expiresInMinutes = expiresInMinutes;
    }

    /**
     * Nested document object serialized as {@code {"content":"...", "filename":"..."}}.
     */
    private static class EnvelopeDocument {

        @SerializedName("content")
        private String content;

        @SerializedName("filename")
        private String filename;

        EnvelopeDocument(String content, String filename) {
            this.content = content;
            this.filename = filename;
        }
    }
}
