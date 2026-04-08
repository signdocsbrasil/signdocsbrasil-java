package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 * Request to create a new signing session.
 */
public class CreateSigningSessionRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("signers")
    private List<SessionSigner> signers;

    @SerializedName("documents")
    private List<SessionDocument> documents;

    @SerializedName("callbackUrl")
    private String callbackUrl;

    @SerializedName("redirectUrl")
    private String redirectUrl;

    @SerializedName("expiresInMinutes")
    private Integer expiresInMinutes;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    @SerializedName("locale")
    private String locale;

    @SerializedName("brandingId")
    private String brandingId;

    public CreateSigningSessionRequest() {
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

    public List<SessionSigner> getSigners() {
        return signers;
    }

    public void setSigners(List<SessionSigner> signers) {
        this.signers = signers;
    }

    public List<SessionDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<SessionDocument> documents) {
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

    public Integer getExpiresInMinutes() {
        return expiresInMinutes;
    }

    public void setExpiresInMinutes(Integer expiresInMinutes) {
        this.expiresInMinutes = expiresInMinutes;
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

    /**
     * A signer participating in the signing session.
     */
    public static class SessionSigner {

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

        @SerializedName("authMethods")
        private List<String> authMethods;

        public SessionSigner() {
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

        public List<String> getAuthMethods() {
            return authMethods;
        }

        public void setAuthMethods(List<String> authMethods) {
            this.authMethods = authMethods;
        }
    }

    /**
     * A document included in the signing session.
     */
    public static class SessionDocument {

        @SerializedName("name")
        private String name;

        @SerializedName("content")
        private String content;

        @SerializedName("contentType")
        private String contentType;

        @SerializedName("externalId")
        private String externalId;

        public SessionDocument() {
        }

        public SessionDocument(String name, String content) {
            this.name = name;
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
    }
}
