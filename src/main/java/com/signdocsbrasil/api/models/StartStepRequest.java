package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request to start a verification step.
 */
public class StartStepRequest {

    @SerializedName("captureMode")
    private String captureMode;

    @SerializedName("otpChannel")
    private String otpChannel;

    public StartStepRequest() {
    }

    public StartStepRequest(String captureMode) {
        this.captureMode = captureMode;
    }

    public StartStepRequest(String captureMode, String otpChannel) {
        this.captureMode = captureMode;
        this.otpChannel = otpChannel;
    }

    public String getCaptureMode() {
        return captureMode;
    }

    public void setCaptureMode(String captureMode) {
        this.captureMode = captureMode;
    }

    public String getOtpChannel() {
        return otpChannel;
    }

    public void setOtpChannel(String otpChannel) {
        this.otpChannel = otpChannel;
    }
}
