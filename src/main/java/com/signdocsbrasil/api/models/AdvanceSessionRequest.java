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

    @SerializedName("otpChannel")
    private String otpChannel;

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

    /** CPF or CNPJ the signer types to confirm their identity ({@code confirm_signer}). */
    @SerializedName("cpfCnpj")
    private String cpfCnpj;

    /** Base64 identity-document photo, max 5MB ({@code complete_document_photo}). */
    @SerializedName("documentImage")
    private String documentImage;

    @SerializedName("documentType")
    private String documentType;

    /**
     * Sandbox-only simulated scores, so a rejection can be rehearsed. Read only
     * once the step already resolved to sandbox — they can never make a real
     * verification pass. Boxed so an unset score is omitted rather than sent as
     * 0, which is a meaningful value here.
     */
    @SerializedName("sandboxSimilarity")
    private Double sandboxSimilarity;

    @SerializedName("sandboxLivenessConfidence")
    private Double sandboxLivenessConfidence;

    @SerializedName("sandboxBrightness")
    private Double sandboxBrightness;

    @SerializedName("sandboxSharpness")
    private Double sandboxSharpness;

    @SerializedName("deviceInfo")
    private DeviceInfo deviceInfo;

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

    public String getOtpChannel() {
        return otpChannel;
    }

    public void setOtpChannel(String otpChannel) {
        this.otpChannel = otpChannel;
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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Double getSandboxSimilarity() {
        return sandboxSimilarity;
    }

    public void setSandboxSimilarity(Double sandboxSimilarity) {
        this.sandboxSimilarity = sandboxSimilarity;
    }

    public Double getSandboxLivenessConfidence() {
        return sandboxLivenessConfidence;
    }

    public void setSandboxLivenessConfidence(Double sandboxLivenessConfidence) {
        this.sandboxLivenessConfidence = sandboxLivenessConfidence;
    }

    public Double getSandboxBrightness() {
        return sandboxBrightness;
    }

    public void setSandboxBrightness(Double sandboxBrightness) {
        this.sandboxBrightness = sandboxBrightness;
    }

    public Double getSandboxSharpness() {
        return sandboxSharpness;
    }

    public void setSandboxSharpness(Double sandboxSharpness) {
        this.sandboxSharpness = sandboxSharpness;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
