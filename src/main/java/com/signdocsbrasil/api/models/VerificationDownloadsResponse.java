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
     */
    public static class Downloads {
        @SerializedName("evidencePack")
        private DownloadArtifact evidencePack;

        @SerializedName("signedPdf")
        private DownloadArtifact signedPdf;

        @SerializedName("finalPdf")
        private DownloadArtifact finalPdf;

        public DownloadArtifact getEvidencePack() {
            return evidencePack;
        }

        public void setEvidencePack(DownloadArtifact evidencePack) {
            this.evidencePack = evidencePack;
        }

        public DownloadArtifact getSignedPdf() {
            return signedPdf;
        }

        public void setSignedPdf(DownloadArtifact signedPdf) {
            this.signedPdf = signedPdf;
        }

        public DownloadArtifact getFinalPdf() {
            return finalPdf;
        }

        public void setFinalPdf(DownloadArtifact finalPdf) {
            this.finalPdf = finalPdf;
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
