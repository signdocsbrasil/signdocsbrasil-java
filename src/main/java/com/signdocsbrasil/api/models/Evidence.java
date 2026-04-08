package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 * Evidence record for a completed transaction.
 */
public class Evidence {

    @SerializedName("tenantId")
    private String tenantId;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("evidenceId")
    private String evidenceId;

    @SerializedName("status")
    private String status;

    @SerializedName("signer")
    private EvidenceSigner signer;

    @SerializedName("steps")
    private List<EvidenceStep> steps;

    @SerializedName("document")
    private EvidenceDocument document;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("completedAt")
    private String completedAt;

    public Evidence() {
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EvidenceSigner getSigner() {
        return signer;
    }

    public void setSigner(EvidenceSigner signer) {
        this.signer = signer;
    }

    public List<EvidenceStep> getSteps() {
        return steps;
    }

    public void setSteps(List<EvidenceStep> steps) {
        this.steps = steps;
    }

    public EvidenceDocument getDocument() {
        return document;
    }

    public void setDocument(EvidenceDocument document) {
        this.document = document;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * Signer information within evidence.
     */
    public static class EvidenceSigner {
        @SerializedName("name")
        private String name;

        @SerializedName("cpf")
        private String cpf;

        @SerializedName("cnpj")
        private String cnpj;

        @SerializedName("userExternalId")
        private String userExternalId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        public String getCnpj() {
            return cnpj;
        }

        public void setCnpj(String cnpj) {
            this.cnpj = cnpj;
        }

        public String getUserExternalId() {
            return userExternalId;
        }

        public void setUserExternalId(String userExternalId) {
            this.userExternalId = userExternalId;
        }
    }

    /**
     * Step information within evidence.
     */
    public static class EvidenceStep {
        @SerializedName("type")
        private String type;

        @SerializedName("status")
        private String status;

        @SerializedName("completedAt")
        private String completedAt;

        @SerializedName("result")
        private Map<String, Object> result;

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

        public String getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(String completedAt) {
            this.completedAt = completedAt;
        }

        public Map<String, Object> getResult() {
            return result;
        }

        public void setResult(Map<String, Object> result) {
            this.result = result;
        }
    }

    /**
     * Document information within evidence.
     */
    public static class EvidenceDocument {
        @SerializedName("hash")
        private String hash;

        @SerializedName("filename")
        private String filename;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }
}
