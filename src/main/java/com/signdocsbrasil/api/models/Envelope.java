package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an envelope.
 */
public class Envelope {

    @SerializedName("envelopeId")
    private String envelopeId;

    @SerializedName("status")
    private String status;

    @SerializedName("signingMode")
    private String signingMode;

    @SerializedName("totalSigners")
    private int totalSigners;

    @SerializedName("documentHash")
    private String documentHash;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("expiresAt")
    private String expiresAt;

    public Envelope() {
    }

    public String getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(String envelopeId) {
        this.envelopeId = envelopeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "Envelope{envelopeId='" + envelopeId + "', status='" + status + "', signingMode='" + signingMode + "'}";
    }
}
