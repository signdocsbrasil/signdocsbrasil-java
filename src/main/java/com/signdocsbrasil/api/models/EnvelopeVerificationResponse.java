package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Public verification response for a multi-signer envelope, returned by
 * {@code GET /v1/verify/envelope/{envelopeId}}.
 *
 * <p>For non-PDF envelopes signed with digital certificates, the consolidated
 * {@code .p7s} containing every signer's {@code SignerInfo} is exposed via
 * {@link Downloads#getConsolidatedSignature()}.
 */
public class EnvelopeVerificationResponse {

    @SerializedName("envelopeId")
    private String envelopeId;

    @SerializedName("status")
    private String status;

    @SerializedName("signingMode")
    private String signingMode;

    @SerializedName("totalSigners")
    private int totalSigners;

    @SerializedName("completedSessions")
    private int completedSessions;

    @SerializedName("documentHash")
    private String documentHash;

    @SerializedName("tenantName")
    private String tenantName;

    @SerializedName("tenantCnpj")
    private String tenantCnpj;

    @SerializedName("signers")
    private List<EnvelopeSigner> signers;

    @SerializedName("downloads")
    private Downloads downloads;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("completedAt")
    private String completedAt;

    public EnvelopeVerificationResponse() {
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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantCnpj() {
        return tenantCnpj;
    }

    public void setTenantCnpj(String tenantCnpj) {
        this.tenantCnpj = tenantCnpj;
    }

    public List<EnvelopeSigner> getSigners() {
        return signers;
    }

    public void setSigners(List<EnvelopeSigner> signers) {
        this.signers = signers;
    }

    public Downloads getDownloads() {
        return downloads;
    }

    public void setDownloads(Downloads downloads) {
        this.downloads = downloads;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * Per-signer entry within an envelope verification response. The
     * {@code evidenceId} is populated for completed signers and can be used
     * with {@link com.signdocsbrasil.api.resources.VerificationResource#verify}
     * to drill down into the individual evidence record.
     */
    public static class EnvelopeSigner {
        @SerializedName("signerIndex")
        private int signerIndex;

        @SerializedName("displayName")
        private String displayName;

        @SerializedName("cpfCnpj")
        private String cpfCnpj;

        @SerializedName("status")
        private String status;

        @SerializedName("policyProfile")
        private String policyProfile;

        @SerializedName("evidenceId")
        private String evidenceId;

        @SerializedName("completedAt")
        private String completedAt;

        public int getSignerIndex() {
            return signerIndex;
        }

        public void setSignerIndex(int signerIndex) {
            this.signerIndex = signerIndex;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getCpfCnpj() {
            return cpfCnpj;
        }

        public void setCpfCnpj(String cpfCnpj) {
            this.cpfCnpj = cpfCnpj;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPolicyProfile() {
            return policyProfile;
        }

        public void setPolicyProfile(String policyProfile) {
            this.policyProfile = policyProfile;
        }

        public String getEvidenceId() {
            return evidenceId;
        }

        public void setEvidenceId(String evidenceId) {
            this.evidenceId = evidenceId;
        }

        public String getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(String completedAt) {
            this.completedAt = completedAt;
        }
    }

    /**
     * Envelope-level consolidated downloads. {@code combinedSignedPdf} is
     * populated for PDF envelopes; {@code consolidatedSignature} is the
     * merged {@code .p7s} for non-PDF envelopes signed with digital
     * certificates.
     */
    public static class Downloads {
        @SerializedName("combinedSignedPdf")
        private VerificationDownloadsResponse.DownloadArtifact combinedSignedPdf;

        @SerializedName("consolidatedSignature")
        private VerificationDownloadsResponse.DownloadArtifact consolidatedSignature;

        public VerificationDownloadsResponse.DownloadArtifact getCombinedSignedPdf() {
            return combinedSignedPdf;
        }

        public void setCombinedSignedPdf(VerificationDownloadsResponse.DownloadArtifact combinedSignedPdf) {
            this.combinedSignedPdf = combinedSignedPdf;
        }

        public VerificationDownloadsResponse.DownloadArtifact getConsolidatedSignature() {
            return consolidatedSignature;
        }

        public void setConsolidatedSignature(VerificationDownloadsResponse.DownloadArtifact consolidatedSignature) {
            this.consolidatedSignature = consolidatedSignature;
        }
    }
}
