package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response containing download URLs for evidence documents.
 */
public class VerificationDownloadsResponse {

    @SerializedName("evidenceId")
    private String evidenceId;

    @SerializedName("downloads")
    private Downloads downloads;

    public VerificationDownloadsResponse() {
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
    }

    public Downloads getDownloads() {
        return downloads;
    }

    public void setDownloads(Downloads downloads) {
        this.downloads = downloads;
    }

    /**
     * Container for download artifacts.
     *
     * <p>{@code signedSignature} is the detached PKCS#7 / CMS ({@code .p7s})
     * for digital-cert signing of non-PDF documents. It is only populated by
     * the API for <strong>standalone signing sessions</strong> (single-signer);
     * it is omitted entirely from the response when the evidence belongs to a
     * multi-signer envelope. Use
     * {@link com.signdocsbrasil.api.resources.VerificationResource#verifyEnvelope}
     * to retrieve the consolidated envelope-level {@code .p7s} instead.
     */
    public static class Downloads {
        @SerializedName("originalDocument")
        private DownloadArtifact originalDocument;

        @SerializedName("evidencePack")
        private DownloadArtifact evidencePack;

        @SerializedName("finalPdf")
        private DownloadArtifact finalPdf;

        @SerializedName("signedSignature")
        private DownloadArtifact signedSignature;

        public DownloadArtifact getOriginalDocument() {
            return originalDocument;
        }

        public void setOriginalDocument(DownloadArtifact originalDocument) {
            this.originalDocument = originalDocument;
        }

        public DownloadArtifact getEvidencePack() {
            return evidencePack;
        }

        public void setEvidencePack(DownloadArtifact evidencePack) {
            this.evidencePack = evidencePack;
        }

        public DownloadArtifact getFinalPdf() {
            return finalPdf;
        }

        public void setFinalPdf(DownloadArtifact finalPdf) {
            this.finalPdf = finalPdf;
        }

        public DownloadArtifact getSignedSignature() {
            return signedSignature;
        }

        public void setSignedSignature(DownloadArtifact signedSignature) {
            this.signedSignature = signedSignature;
        }
    }

    /**
     * A downloadable artifact with URL and filename.
     */
    public static class DownloadArtifact {
        @SerializedName("url")
        private String url;

        @SerializedName("filename")
        private String filename;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }
}
