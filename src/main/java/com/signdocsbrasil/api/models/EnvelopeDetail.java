package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Detailed envelope representation including session summaries.
 */
public class EnvelopeDetail {

    @SerializedName("envelopeId")
    private String envelopeId;

    @SerializedName("status")
    private String status;

    @SerializedName("signingMode")
    private String signingMode;

    @SerializedName("totalSigners")
    private int totalSigners;

    @SerializedName("addedSessions")
    private int addedSessions;

    @SerializedName("completedSessions")
    private int completedSessions;

    @SerializedName("documentHash")
    private String documentHash;

    @SerializedName("sessions")
    private List<EnvelopeSessionSummary> sessions;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("expiresAt")
    private String expiresAt;

    @SerializedName("combinedSignedPdfUrl")
    private String combinedSignedPdfUrl;

    public EnvelopeDetail() {
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

    public int getAddedSessions() {
        return addedSessions;
    }

    public void setAddedSessions(int addedSessions) {
        this.addedSessions = addedSessions;
    }

    public int getCompletedSessions() {
        return completedSessions;
    }

    public void setCompletedSessions(int completedSessions) {
        this.completedSessions = completedSessions;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public List<EnvelopeSessionSummary> getSessions() {
        return sessions;
    }

    public void setSessions(List<EnvelopeSessionSummary> sessions) {
        this.sessions = sessions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getCombinedSignedPdfUrl() {
        return combinedSignedPdfUrl;
    }

    public void setCombinedSignedPdfUrl(String combinedSignedPdfUrl) {
        this.combinedSignedPdfUrl = combinedSignedPdfUrl;
    }

    @Override
    public String toString() {
        return "EnvelopeDetail{envelopeId='" + envelopeId + "', status='" + status
                + "', totalSigners=" + totalSigners + ", completedSessions=" + completedSessions + "}";
    }
}
