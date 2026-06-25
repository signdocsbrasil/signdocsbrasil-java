package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * A single signature detected within a PDF document, as returned by
 * {@code POST /v1/verify/document} inside {@link VerifyDocumentResponse}.
 */
public class DetectedSignature {

    @SerializedName("method")
    private String method;

    /**
     * The signature type. One of {@code "pades"}, {@code "pkcs7"},
     * {@code "legacy"}, or {@code "digital_certificate"}.
     */
    @SerializedName("type")
    private String type;

    @SerializedName("subFilter")
    private String subFilter;

    @SerializedName("filter")
    private String filter;

    @SerializedName("confidence")
    private double confidence;

    public DetectedSignature() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubFilter() {
        return subFilter;
    }

    public void setSubFilter(String subFilter) {
        this.subFilter = subFilter;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
