package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from starting a verification step.
 */
public class StartStepResponse {

    @SerializedName("stepId")
    private String stepId;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("livenessSessionId")
    private String livenessSessionId;

    @SerializedName("hostedUrl")
    private String hostedUrl;

    @SerializedName("message")
    private String message;

    @SerializedName("otpCode")
    private String otpCode;

    public StartStepResponse() {
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLivenessSessionId() {
        return livenessSessionId;
    }

    public void setLivenessSessionId(String livenessSessionId) {
        this.livenessSessionId = livenessSessionId;
    }

    public String getHostedUrl() {
        return hostedUrl;
    }

    public void setHostedUrl(String hostedUrl) {
        this.hostedUrl = hostedUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
