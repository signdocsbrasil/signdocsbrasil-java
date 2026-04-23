package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Request to create a new envelope for multi-signer signing.
 *
 * <p>An envelope bundles a single document with an ordered or parallel
 * set of signer sessions. The document content and signing mode are
 * fixed at creation time; individual signer sessions are added later
 * via {@code POST /v1/envelopes/{id}/sessions}.
 */
public class CreateEnvelopeRequest {

    /** Either {@code PARALLEL} or {@code SEQUENTIAL}. */
    @SerializedName("signingMode")
    private String signingMode;

    @SerializedName("totalSigners")
    private int totalSigners;

    @SerializedName("document")
    private EnvelopeDocument document;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    @SerializedName("locale")
    private String locale;

    @SerializedName("returnUrl")
    private String returnUrl;

    @SerializedName("cancelUrl")
    private String cancelUrl;

    @SerializedName("expiresInMinutes")
    private Integer expiresInMinutes;

    /**
     * When provided, SignDocs automatically sends an invite email to
     * each signer as they are added to the envelope (if their email
     * differs from the owner's), and notifies the owner on every signer
     * completion plus a final "all signed" message. See {@link Owner}.
     */
    @SerializedName("owner")
    private Owner owner;

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

    public EnvelopeDocument getDocument() {
        return document;
    }

    public void setDocument(EnvelopeDocument document) {
        this.document = document;
    }

    /**
     * Convenience setter that wraps the given content and filename in an
     * {@link EnvelopeDocument}.
     *
     * @param content  the base64-encoded document content
     * @param filename the document filename, or {@code null}
     */
    public void setDocument(String content, String filename) {
        this.document = new EnvelopeDocument(content, filename);
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

    public Integer getExpiresInMinutes() {
        return expiresInMinutes;
    }

    public void setExpiresInMinutes(Integer expiresInMinutes) {
        this.expiresInMinutes = expiresInMinutes;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Inline document payload serialized as
     * {@code {"content":"<base64>", "filename":"..."}}.
     */
    public static class EnvelopeDocument {

        @SerializedName("content")
        private String content;

        @SerializedName("filename")
        private String filename;

        public EnvelopeDocument() {
        }

        public EnvelopeDocument(String content) {
            this.content = content;
        }

        public EnvelopeDocument(String content, String filename) {
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
}
