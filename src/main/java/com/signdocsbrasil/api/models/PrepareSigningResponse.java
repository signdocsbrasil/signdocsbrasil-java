package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from preparing a digital signing operation.
 * Contains the hash that must be signed by the client's private key.
 */
public class PrepareSigningResponse {

    @SerializedName("signatureRequestId")
    private String signatureRequestId;

    @SerializedName("hashToSign")
    private String hashToSign;

    @SerializedName("hashAlgorithm")
    private String hashAlgorithm;

    @SerializedName("signatureAlgorithm")
    private String signatureAlgorithm;

    public PrepareSigningResponse() {
    }

    public String getSignatureRequestId() {
        return signatureRequestId;
    }

    public void setSignatureRequestId(String signatureRequestId) {
        this.signatureRequestId = signatureRequestId;
    }

    public String getHashToSign() {
        return hashToSign;
    }

    public void setHashToSign(String hashToSign) {
        this.hashToSign = hashToSign;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }
}
