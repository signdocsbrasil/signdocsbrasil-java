package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from completing a digital signing operation.
 */
public class CompleteSigningResponse {

    @SerializedName("stepId")
    private String stepId;

    @SerializedName("status")
    private String status;

    @SerializedName("result")
    private SigningResult result;

    public CompleteSigningResponse() {
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SigningResult getResult() {
        return result;
    }

    public void setResult(SigningResult result) {
        this.result = result;
    }

    /**
     * The signing result containing digital signature details.
     */
    public static class SigningResult {
        @SerializedName("digitalSignature")
        private StepResult.DigitalSignatureResult digitalSignature;

        public StepResult.DigitalSignatureResult getDigitalSignature() {
            return digitalSignature;
        }

        public void setDigitalSignature(StepResult.DigitalSignatureResult digitalSignature) {
            this.digitalSignature = digitalSignature;
        }
    }
}
