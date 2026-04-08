package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Request to create a new signing transaction.
 */
public class CreateTransactionRequest {

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("policy")
    private Policy policy;

    @SerializedName("signer")
    private Signer signer;

    @SerializedName("document")
    private InlineDocument document;

    @SerializedName("action")
    private ActionMetadata action;

    @SerializedName("digitalSignature")
    private DigitalSignatureMetadata digitalSignature;

    @SerializedName("documentGroupId")
    private String documentGroupId;

    @SerializedName("signerIndex")
    private Integer signerIndex;

    @SerializedName("totalSigners")
    private Integer totalSigners;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    @SerializedName("expiresInMinutes")
    private Integer expiresInMinutes;

    public CreateTransactionRequest() {
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

    public InlineDocument getDocument() {
        return document;
    }

    public void setDocument(InlineDocument document) {
        this.document = document;
    }

    public ActionMetadata getAction() {
        return action;
    }

    public void setAction(ActionMetadata action) {
        this.action = action;
    }

    public DigitalSignatureMetadata getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(DigitalSignatureMetadata digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public String getDocumentGroupId() {
        return documentGroupId;
    }

    public void setDocumentGroupId(String documentGroupId) {
        this.documentGroupId = documentGroupId;
    }

    public Integer getSignerIndex() {
        return signerIndex;
    }

    public void setSignerIndex(Integer signerIndex) {
        this.signerIndex = signerIndex;
    }

    public Integer getTotalSigners() {
        return totalSigners;
    }

    public void setTotalSigners(Integer totalSigners) {
        this.totalSigners = totalSigners;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Integer getExpiresInMinutes() {
        return expiresInMinutes;
    }

    public void setExpiresInMinutes(Integer expiresInMinutes) {
        this.expiresInMinutes = expiresInMinutes;
    }

    /**
     * Inline document content included directly in the transaction creation request.
     */
    public static class InlineDocument {
        @SerializedName("content")
        private String content;

        @SerializedName("filename")
        private String filename;

        public InlineDocument() {
        }

        public InlineDocument(String content) {
            this.content = content;
        }

        public InlineDocument(String content, String filename) {
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
     * Metadata for action authentication transactions.
     */
    public static class ActionMetadata {
        @SerializedName("type")
        private String type;

        @SerializedName("description")
        private String description;

        @SerializedName("reference")
        private String reference;

        public ActionMetadata() {
        }

        public ActionMetadata(String type, String description) {
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
     * Metadata for digital signature configuration.
     */
    public static class DigitalSignatureMetadata {
        @SerializedName("signatureFieldName")
        private String signatureFieldName;

        @SerializedName("signatureReason")
        private String signatureReason;

        @SerializedName("signatureLocation")
        private String signatureLocation;

        public DigitalSignatureMetadata() {
        }

        public String getSignatureFieldName() {
            return signatureFieldName;
        }

        public void setSignatureFieldName(String signatureFieldName) {
            this.signatureFieldName = signatureFieldName;
        }

        public String getSignatureReason() {
            return signatureReason;
        }

        public void setSignatureReason(String signatureReason) {
            this.signatureReason = signatureReason;
        }

        public String getSignatureLocation() {
            return signatureLocation;
        }

        public void setSignatureLocation(String signatureLocation) {
            this.signatureLocation = signatureLocation;
        }
    }
}
