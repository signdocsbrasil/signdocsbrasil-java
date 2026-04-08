package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Request to add a signing session to an envelope.
 */
public class AddEnvelopeSessionRequest {

    @SerializedName("signer")
    private EnvelopeSigner signer;

    @SerializedName("policy")
    private EnvelopePolicy policy;

    @SerializedName("purpose")
    private String purpose;

    @SerializedName("signerIndex")
    private int signerIndex;

    @SerializedName("returnUrl")
    private String returnUrl;

    @SerializedName("cancelUrl")
    private String cancelUrl;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    public AddEnvelopeSessionRequest() {
        this.signer = new EnvelopeSigner();
        this.signer.userExternalId = "sdk";
        this.policy = new EnvelopePolicy();
        this.policy.profile = "CLICK_ONLY";
        this.purpose = "DOCUMENT_SIGNATURE";
        this.signerIndex = 1;
    }

    public String getSignerName() {
        return signer != null ? signer.name : null;
    }

    public void setSignerName(String name) {
        ensureSigner();
        this.signer.name = name;
    }

    public String getSignerUserExternalId() {
        return signer != null ? signer.userExternalId : null;
    }

    public void setSignerUserExternalId(String userExternalId) {
        ensureSigner();
        this.signer.userExternalId = userExternalId;
    }

    public String getSignerCpf() {
        return signer != null ? signer.cpf : null;
    }

    public void setSignerCpf(String cpf) {
        ensureSigner();
        this.signer.cpf = cpf;
    }

    public String getSignerCnpj() {
        return signer != null ? signer.cnpj : null;
    }

    public void setSignerCnpj(String cnpj) {
        ensureSigner();
        this.signer.cnpj = cnpj;
    }

    public String getSignerEmail() {
        return signer != null ? signer.email : null;
    }

    public void setSignerEmail(String email) {
        ensureSigner();
        this.signer.email = email;
    }

    public String getSignerPhone() {
        return signer != null ? signer.phone : null;
    }

    public void setSignerPhone(String phone) {
        ensureSigner();
        this.signer.phone = phone;
    }

    public String getSignerBirthDate() {
        return signer != null ? signer.birthDate : null;
    }

    public void setSignerBirthDate(String birthDate) {
        ensureSigner();
        this.signer.birthDate = birthDate;
    }

    public String getSignerOtpChannel() {
        return signer != null ? signer.otpChannel : null;
    }

    public void setSignerOtpChannel(String otpChannel) {
        ensureSigner();
        this.signer.otpChannel = otpChannel;
    }

    public String getPolicyProfile() {
        return policy != null ? policy.profile : null;
    }

    public void setPolicyProfile(String profile) {
        ensurePolicy();
        this.policy.profile = profile;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getSignerIndex() {
        return signerIndex;
    }

    public void setSignerIndex(int signerIndex) {
        this.signerIndex = signerIndex;
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

    private void ensureSigner() {
        if (this.signer == null) {
            this.signer = new EnvelopeSigner();
        }
    }

    private void ensurePolicy() {
        if (this.policy == null) {
            this.policy = new EnvelopePolicy();
        }
    }

    /**
     * Nested signer object serialized as {@code {"name":"...", "cpf":"...", ...}}.
     */
    private static class EnvelopeSigner {

        @SerializedName("name")
        private String name;

        @SerializedName("userExternalId")
        private String userExternalId;

        @SerializedName("cpf")
        private String cpf;

        @SerializedName("cnpj")
        private String cnpj;

        @SerializedName("email")
        private String email;

        @SerializedName("phone")
        private String phone;

        @SerializedName("birthDate")
        private String birthDate;

        @SerializedName("otpChannel")
        private String otpChannel;

        EnvelopeSigner() {
        }
    }

    /**
     * Nested policy object serialized as {@code {"profile":"..."}}.
     */
    private static class EnvelopePolicy {

        @SerializedName("profile")
        private String profile;

        EnvelopePolicy() {
        }
    }
}
