package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Full bootstrap data for a signing session.
 * Returned by GET /v1/signing-sessions/{sessionId}.
 */
public class SigningSessionBootstrap {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("status")
    private String status;

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("signer")
    private BootstrapSigner signer;

    @SerializedName("steps")
    private List<BootstrapStep> steps;

    @SerializedName("locale")
    private String locale;

    @SerializedName("expiresAt")
    private String expiresAt;

    @SerializedName("document")
    private BootstrapDocument document;

    @SerializedName("action")
    private BootstrapAction action;

    @SerializedName("appearance")
    private BootstrapAppearance appearance;

    @SerializedName("returnUrl")
    private String returnUrl;

    @SerializedName("cancelUrl")
    private String cancelUrl;

    public SigningSessionBootstrap() {
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public BootstrapSigner getSigner() { return signer; }
    public void setSigner(BootstrapSigner signer) { this.signer = signer; }

    public List<BootstrapStep> getSteps() { return steps; }
    public void setSteps(List<BootstrapStep> steps) { this.steps = steps; }

    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }

    public String getExpiresAt() { return expiresAt; }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }

    public BootstrapDocument getDocument() { return document; }
    public void setDocument(BootstrapDocument document) { this.document = document; }

    public BootstrapAction getAction() { return action; }
    public void setAction(BootstrapAction action) { this.action = action; }

    public BootstrapAppearance getAppearance() { return appearance; }
    public void setAppearance(BootstrapAppearance appearance) { this.appearance = appearance; }

    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }

    public String getCancelUrl() { return cancelUrl; }
    public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }

    @Override
    public String toString() {
        return "SigningSessionBootstrap{sessionId='" + sessionId + "', status='" + status + "'}";
    }

    public static class BootstrapSigner {

        @SerializedName("name")
        private String name;

        @SerializedName("maskedEmail")
        private String maskedEmail;

        @SerializedName("maskedCpf")
        private String maskedCpf;

        public BootstrapSigner() {
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getMaskedEmail() { return maskedEmail; }
        public void setMaskedEmail(String maskedEmail) { this.maskedEmail = maskedEmail; }

        public String getMaskedCpf() { return maskedCpf; }
        public void setMaskedCpf(String maskedCpf) { this.maskedCpf = maskedCpf; }
    }

    public static class BootstrapStep {

        @SerializedName("stepId")
        private String stepId;

        @SerializedName("type")
        private String type;

        @SerializedName("status")
        private String status;

        @SerializedName("order")
        private int order;

        public BootstrapStep() {
        }

        public String getStepId() { return stepId; }
        public void setStepId(String stepId) { this.stepId = stepId; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }
    }

    public static class BootstrapDocument {

        @SerializedName("presignedUrl")
        private String presignedUrl;

        @SerializedName("filename")
        private String filename;

        @SerializedName("hash")
        private String hash;

        public BootstrapDocument() {
        }

        public String getPresignedUrl() { return presignedUrl; }
        public void setPresignedUrl(String presignedUrl) { this.presignedUrl = presignedUrl; }

        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }

        public String getHash() { return hash; }
        public void setHash(String hash) { this.hash = hash; }
    }

    public static class BootstrapAction {

        @SerializedName("type")
        private String type;

        @SerializedName("description")
        private String description;

        @SerializedName("reference")
        private String reference;

        public BootstrapAction() {
        }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
    }

    public static class BootstrapAppearance {

        @SerializedName("brandColor")
        private String brandColor;

        @SerializedName("logoUrl")
        private String logoUrl;

        @SerializedName("companyName")
        private String companyName;

        @SerializedName("backgroundColor")
        private String backgroundColor;

        @SerializedName("textColor")
        private String textColor;

        @SerializedName("buttonTextColor")
        private String buttonTextColor;

        @SerializedName("borderRadius")
        private String borderRadius;

        @SerializedName("headerStyle")
        private String headerStyle;

        @SerializedName("fontFamily")
        private String fontFamily;

        public BootstrapAppearance() {
        }

        public String getBrandColor() { return brandColor; }
        public void setBrandColor(String brandColor) { this.brandColor = brandColor; }

        public String getLogoUrl() { return logoUrl; }
        public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }

        public String getBackgroundColor() { return backgroundColor; }
        public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

        public String getTextColor() { return textColor; }
        public void setTextColor(String textColor) { this.textColor = textColor; }

        public String getButtonTextColor() { return buttonTextColor; }
        public void setButtonTextColor(String buttonTextColor) { this.buttonTextColor = buttonTextColor; }

        public String getBorderRadius() { return borderRadius; }
        public void setBorderRadius(String borderRadius) { this.borderRadius = borderRadius; }

        public String getHeaderStyle() { return headerStyle; }
        public void setHeaderStyle(String headerStyle) { this.headerStyle = headerStyle; }

        public String getFontFamily() { return fontFamily; }
        public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }
    }
}
