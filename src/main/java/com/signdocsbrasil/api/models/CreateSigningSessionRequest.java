package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Request to create a new signing session.
 *
 * <p>Signing sessions are the preferred way to kick off a single-signer
 * flow. The request carries the {@code purpose} (document signature or
 * action authentication), the verification {@code policy}, the
 * {@code signer} identity, and optionally an inline {@code document},
 * {@code action} metadata, branding {@code appearance}, and an
 * {@code owner} to opt-in to server-managed invitation/completion
 * emails.
 */
public class CreateSigningSessionRequest {

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("policy")
    private Policy policy;

    @SerializedName("signer")
    private Signer signer;

    @SerializedName("document")
    private SessionDocument document;

    @SerializedName("action")
    private SessionAction action;

    @SerializedName("returnUrl")
    private String returnUrl;

    @SerializedName("cancelUrl")
    private String cancelUrl;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    @SerializedName("locale")
    private String locale;

    @SerializedName("expiresInMinutes")
    private Integer expiresInMinutes;

    @SerializedName("appearance")
    private SessionAppearance appearance;

    /** See {@link Owner} for behavior when set. */
    @SerializedName("owner")
    private Owner owner;

    public CreateSigningSessionRequest() {
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Signer getSigner() {
        return signer;
    }

    public void setSigner(Signer signer) {
        this.signer = signer;
    }

    public SessionDocument getDocument() {
        return document;
    }

    public void setDocument(SessionDocument document) {
        this.document = document;
    }

    public SessionAction getAction() {
        return action;
    }

    public void setAction(SessionAction action) {
        this.action = action;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getExpiresInMinutes() {
        return expiresInMinutes;
    }

    public void setExpiresInMinutes(Integer expiresInMinutes) {
        this.expiresInMinutes = expiresInMinutes;
    }

    public SessionAppearance getAppearance() {
        return appearance;
    }

    public void setAppearance(SessionAppearance appearance) {
        this.appearance = appearance;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Inline document attached to the signing session, serialized as
     * {@code {"content":"<base64>", "filename":"..."}}.
     */
    public static class SessionDocument {

        @SerializedName("content")
        private String content;

        @SerializedName("filename")
        private String filename;

        public SessionDocument() {
        }

        public SessionDocument(String content) {
            this.content = content;
        }

        public SessionDocument(String content, String filename) {
            this.content = content;
            this.filename = filename;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }

    /**
     * Action metadata for ACTION_AUTHENTICATION sessions.
     */
    public static class SessionAction {

        @SerializedName("type")
        private String type;

        @SerializedName("description")
        private String description;

        @SerializedName("reference")
        private String reference;

        public SessionAction() {
        }

        public SessionAction(String type, String description) {
            this.type = type;
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }
    }

    /**
     * Branding / appearance configuration for the hosted signing UI.
     */
    public static class SessionAppearance {

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

        public SessionAppearance() {
        }

        public String getBrandColor() {
            return brandColor;
        }

        public void setBrandColor(String brandColor) {
            this.brandColor = brandColor;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getButtonTextColor() {
            return buttonTextColor;
        }

        public void setButtonTextColor(String buttonTextColor) {
            this.buttonTextColor = buttonTextColor;
        }

        public String getBorderRadius() {
            return borderRadius;
        }

        public void setBorderRadius(String borderRadius) {
            this.borderRadius = borderRadius;
        }

        public String getHeaderStyle() {
            return headerStyle;
        }

        public void setHeaderStyle(String headerStyle) {
            this.headerStyle = headerStyle;
        }

        public String getFontFamily() {
            return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
        }
    }
}
