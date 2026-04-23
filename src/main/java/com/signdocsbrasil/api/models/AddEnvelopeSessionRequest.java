package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Request to add a signer session to an envelope.
 *
 * <p>Each call registers one signer — identified by {@code signer} and
 * positioned at {@code signerIndex} (1-based) within the envelope. The
 * {@code policy} controls the verification steps that will be applied
 * to this signer.
 */
public class AddEnvelopeSessionRequest {

    @SerializedName("signer")
    private Signer signer;

    @SerializedName("policy")
    private Policy policy;

    /** Either {@code DOCUMENT_SIGNATURE} or {@code ACTION_AUTHENTICATION}. */
    @SerializedName("purpose")
    private String purpose;

    /** 1-based position of this signer within the envelope. */
    @SerializedName("signerIndex")
    private int signerIndex;

    @SerializedName("returnUrl")
    private String returnUrl;

    @SerializedName("cancelUrl")
    private String cancelUrl;

    @SerializedName("metadata")
    private Map<String, String> metadata;

    public AddEnvelopeSessionRequest() {
        this.signer = new Signer();
        this.signer.setUserExternalId("sdk");
        this.policy = new Policy("CLICK_ONLY");
        this.purpose = "DOCUMENT_SIGNATURE";
        this.signerIndex = 1;
    }

    public Signer getSigner() {
        return signer;
    }

    public void setSigner(Signer signer) {
        this.signer = signer;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
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
}
