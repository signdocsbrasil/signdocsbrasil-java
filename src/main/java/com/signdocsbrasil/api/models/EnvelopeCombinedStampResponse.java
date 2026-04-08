package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from the combined stamp operation on an envelope.
 */
public class EnvelopeCombinedStampResponse {

    @SerializedName("envelopeId")
    private String envelopeId;

    @SerializedName("downloadUrl")
    private String downloadUrl;

    @SerializedName("expiresIn")
    private int expiresIn;

    @SerializedName("signerCount")
    private int signerCount;

    public EnvelopeCombinedStampResponse() {
    }

    public String getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(String envelopeId) {
        this.envelopeId = envelopeId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getSignerCount() {
        return signerCount;
    }

    public void setSignerCount(int signerCount) {
        this.signerCount = signerCount;
    }

    @Override
    public String toString() {
        return "EnvelopeCombinedStampResponse{envelopeId='" + envelopeId + "', signerCount=" + signerCount + "}";
    }
}
