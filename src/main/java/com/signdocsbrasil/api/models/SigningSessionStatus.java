package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Status of a signing session, including per-signer progress.
 */
public class SigningSessionStatus {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("status")
    private String status;

    @SerializedName("signers")
    private List<SignerStatus> signers;

    @SerializedName("completedAt")
    private String completedAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public SigningSessionStatus() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SignerStatus> getSigners() {
        return signers;
    }

    public void setSigners(List<SignerStatus> signers) {
        this.signers = signers;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SigningSessionStatus{sessionId='" + sessionId + "', status='" + status + "'}";
    }

    /**
     * Status of an individual signer within a signing session.
     */
    public static class SignerStatus {

        @SerializedName("signerId")
        private String signerId;

        @SerializedName("name")
        private String name;

        @SerializedName("email")
        private String email;

        @SerializedName("status")
        private String status;

        @SerializedName("signedAt")
        private String signedAt;

        @SerializedName("viewedAt")
        private String viewedAt;

        public SignerStatus() {
        }

        public String getSignerId() {
            return signerId;
        }

        public void setSignerId(String signerId) {
            this.signerId = signerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSignedAt() {
            return signedAt;
        }

        public void setSignedAt(String signedAt) {
            this.signedAt = signedAt;
        }

        public String getViewedAt() {
            return viewedAt;
        }

        public void setViewedAt(String viewedAt) {
            this.viewedAt = viewedAt;
        }
    }
}
