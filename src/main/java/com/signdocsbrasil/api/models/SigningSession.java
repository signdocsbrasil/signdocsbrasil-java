package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 * Represents a signing session.
 */
public class SigningSession {

    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("tenantId")
    private String tenantId;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("signers")
    private List<SigningSessionSigner> signers;

    @SerializedName("documents")
    private List<SigningSessionDocument> documents;

    @SerializedName("callbackUrl")
    private String callbackUrl;

    @SerializedName("redirectUrl")
    private String redirectUrl;

    @SerializedName("sessionUrl")
    private String sessionUrl;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    @SerializedName("locale")
    private String locale;

    @SerializedName("brandingId")
    private String brandingId;

    @SerializedName("expiresAt")
    private String expiresAt;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public SigningSession() {
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<SigningSessionSigner> getSigners() {
        return signers;
    }

    public void setSigners(List<SigningSessionSigner> signers) {
        this.signers = signers;
    }

    public List<SigningSessionDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<SigningSessionDocument> documents) {
        this.documents = documents;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getSessionUrl() {
        return sessionUrl;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
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

    public String getBrandingId() {
        return brandingId;
    }

    public void setBrandingId(String brandingId) {
        this.brandingId = brandingId;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "SigningSession{sessionId='" + sessionId + "', status='" + status + "', name='" + name + "'}";
    }

    /**
     * A signer within a signing session response.
     */
    public static class SigningSessionSigner {

        @SerializedName("signerId")
        private String signerId;

        @SerializedName("name")
        private String name;

        @SerializedName("email")
        private String email;

        @SerializedName("cpf")
        private String cpf;

        @SerializedName("phone")
        private String phone;

        @SerializedName("role")
        private String role;

        @SerializedName("order")
        private Integer order;

        @SerializedName("status")
        private String status;

        @SerializedName("signedAt")
        private String signedAt;

        @SerializedName("signerUrl")
        private String signerUrl;

        public SigningSessionSigner() {
        }

        public String getSignerId() {
            return signerId;
        }

        public void setSignerId(String signerId) {
            this.signerId = signerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSignedAt() {
            return signedAt;
        }

        public void setSignedAt(String signedAt) {
            this.signedAt = signedAt;
        }

        public String getSignerUrl() {
            return signerUrl;
        }

        public void setSignerUrl(String signerUrl) {
            this.signerUrl = signerUrl;
        }
    }

    /**
     * A document within a signing session response.
     */
    public static class SigningSessionDocument {

        @SerializedName("documentId")
        private String documentId;

        @SerializedName("name")
        private String name;

        @SerializedName("contentType")
        private String contentType;

        @SerializedName("externalId")
        private String externalId;

        @SerializedName("status")
        private String status;

        public SigningSessionDocument() {
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
