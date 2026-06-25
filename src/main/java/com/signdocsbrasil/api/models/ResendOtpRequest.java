package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request to resend the OTP challenge for a signing session,
 * optionally selecting the delivery channel.
 */
public class ResendOtpRequest {

    @SerializedName("channel")
    private String channel;

    public ResendOtpRequest() {
    }

    public ResendOtpRequest(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
