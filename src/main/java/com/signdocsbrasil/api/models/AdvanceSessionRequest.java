package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Request to advance a signing session step.
 */
public class AdvanceSessionRequest {

    @SerializedName("action")
    private String action;

    @SerializedName("otpCode")
    private String otpCode;

    @SerializedName("livenessSessionId")
    private String livenessSessionId;

    @SerializedName("certificateChainPems")
    private List<String> certificateChainPems;

    @SerializedName("signatureRequestId")
    private String signatureRequestId;

    @SerializedName("rawSignatureBase64")
    private String rawSignatureBase64;

    @SerializedName("geolocation")
    private Geolocation geolocation;

    public AdvanceSessionRequest() {
    }

    public AdvanceSessionRequest(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getLivenessSessionId() {
        return livenessSessionId;
    }

    public void setLivenessSessionId(String livenessSessionId) {
        this.livenessSessionId = livenessSessionId;
    }

    public List<String> getCertificateChainPems() {
        return certificateChainPems;
    }

    public void setCertificateChainPems(List<String> certificateChainPems) {
        this.certificateChainPems = certificateChainPems;
    }

    public String getSignatureRequestId() {
        return signatureRequestId;
    }

    public void setSignatureRequestId(String signatureRequestId) {
        this.signatureRequestId = signatureRequestId;
    }

    public String getRawSignatureBase64() {
        return rawSignatureBase64;
    }

    public void setRawSignatureBase64(String rawSignatureBase64) {
        this.rawSignatureBase64 = rawSignatureBase64;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    /**
     * Geolocation data captured during a signing step.
     */
    public static class Geolocation {

        @SerializedName("latitude")
        private Double latitude;

        @SerializedName("longitude")
        private Double longitude;

        @SerializedName("accuracy")
        private Double accuracy;

        @SerializedName("source")
        private String source;

        public Geolocation() {
        }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }

        public Double getAccuracy() { return accuracy; }
        public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
    }
}
