package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response after advancing a signing session step.
 */
public class AdvanceSessionResponse {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("status")
    private String status;

    @SerializedName("currentStep")
    private StepRef currentStep;

    @SerializedName("nextStep")
    private StepRef nextStep;

    @SerializedName("evidenceId")
    private String evidenceId;

    @SerializedName("redirectUrl")
    private String redirectUrl;

    @SerializedName("completedAt")
    private String completedAt;

    @SerializedName("hostedUrl")
    private String hostedUrl;

    @SerializedName("livenessSessionId")
    private String livenessSessionId;

    @SerializedName("signatureRequestId")
    private String signatureRequestId;

    @SerializedName("hashToSign")
    private String hashToSign;

    @SerializedName("hashAlgorithm")
    private String hashAlgorithm;

    @SerializedName("signatureAlgorithm")
    private String signatureAlgorithm;

    @SerializedName("sandbox")
    private SandboxData sandbox;

    public AdvanceSessionResponse() {
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public StepRef getCurrentStep() { return currentStep; }
    public void setCurrentStep(StepRef currentStep) { this.currentStep = currentStep; }

    public StepRef getNextStep() { return nextStep; }
    public void setNextStep(StepRef nextStep) { this.nextStep = nextStep; }

    public String getEvidenceId() { return evidenceId; }
    public void setEvidenceId(String evidenceId) { this.evidenceId = evidenceId; }

    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }

    public String getCompletedAt() { return completedAt; }
    public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }

    public String getHostedUrl() { return hostedUrl; }
    public void setHostedUrl(String hostedUrl) { this.hostedUrl = hostedUrl; }

    public String getLivenessSessionId() { return livenessSessionId; }
    public void setLivenessSessionId(String livenessSessionId) { this.livenessSessionId = livenessSessionId; }

    public String getSignatureRequestId() { return signatureRequestId; }
    public void setSignatureRequestId(String signatureRequestId) { this.signatureRequestId = signatureRequestId; }

    public String getHashToSign() { return hashToSign; }
    public void setHashToSign(String hashToSign) { this.hashToSign = hashToSign; }

    public String getHashAlgorithm() { return hashAlgorithm; }
    public void setHashAlgorithm(String hashAlgorithm) { this.hashAlgorithm = hashAlgorithm; }

    public String getSignatureAlgorithm() { return signatureAlgorithm; }
    public void setSignatureAlgorithm(String signatureAlgorithm) { this.signatureAlgorithm = signatureAlgorithm; }

    public SandboxData getSandbox() { return sandbox; }
    public void setSandbox(SandboxData sandbox) { this.sandbox = sandbox; }

    @Override
    public String toString() {
        return "AdvanceSessionResponse{sessionId='" + sessionId + "', status='" + status + "'}";
    }

    /**
     * A step reference in the advance response.
     */
    public static class StepRef {

        @SerializedName("stepId")
        private String stepId;

        @SerializedName("type")
        private String type;

        @SerializedName("status")
        private String status;

        public StepRef() {
        }

        public String getStepId() { return stepId; }
        public void setStepId(String stepId) { this.stepId = stepId; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * Sandbox-only data (HML environment).
     */
    public static class SandboxData {

        @SerializedName("otpCode")
        private String otpCode;

        public SandboxData() {
        }

        public String getOtpCode() { return otpCode; }
        public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    }
}
