package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Characteristics of the signer's device, recorded in the evidence alongside
 * geolocation. Every field is optional.
 */
public class DeviceInfo {

    @SerializedName("screenWidth")
    private Integer screenWidth;

    @SerializedName("screenHeight")
    private Integer screenHeight;

    @SerializedName("language")
    private String language;

    @SerializedName("platform")
    private String platform;

    @SerializedName("touchPoints")
    private Integer touchPoints;

    public DeviceInfo() {
    }

    public Integer getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(Integer screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Integer getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getTouchPoints() {
        return touchPoints;
    }

    public void setTouchPoints(Integer touchPoints) {
        this.touchPoints = touchPoints;
    }

    @Override
    public String toString() {
        return "DeviceInfo{platform='" + platform + "', language='" + language + "'}";
    }
}
