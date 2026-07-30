package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response containing download URLs for a document.
 */
public class DownloadResponse {

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("documentHash")
    private String documentHash;

    @SerializedName("originalUrl")
    private String originalUrl;

    /**
     * Signed/stamped document. Present for PDF transactions
     * ({@code documentFormat == "pdf"}), where the signature is embedded.
     */
    @SerializedName("signedUrl")
    private String signedUrl;

    /**
     * Detached CAdES signature ({@code .p7s}). Returned instead of
     * {@code signedUrl} for non-PDF transactions
     * ({@code documentFormat == "generic"}), which cannot carry an embedded
     * signature.
     *
     * <p>Caveat: the API presigns this key without checking that the object
     * exists, so a non-PDF signed under a click/OTP policy still returns a URL
     * here — one that 404s, because only the digital-certificate step writes a
     * {@code .p7s}. Branch on the signing policy, not on this field being set.
     */
    @SerializedName("signatureUrl")
    private String signatureUrl;

    /**
     * {@code "pdf"} or {@code "generic"}, derived by the API from the uploaded
     * bytes rather than the filename.
     */
    @SerializedName("documentFormat")
    private String documentFormat;

    @SerializedName("expiresIn")
    private int expiresIn;

    public DownloadResponse() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getSignedUrl() {
        return signedUrl;
    }

    public void setSignedUrl(String signedUrl) {
        this.signedUrl = signedUrl;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public String getDocumentFormat() {
        return documentFormat;
    }

    public void setDocumentFormat(String documentFormat) {
        this.documentFormat = documentFormat;
    }
}
