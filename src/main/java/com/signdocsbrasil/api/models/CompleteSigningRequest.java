package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request to complete a digital signing operation with the raw signature.
 */
public class CompleteSigningRequest {

    @SerializedName("signatureRequestId")
    private String signatureRequestId;

    @SerializedName("rawSignatureBase64")
    private String rawSignatureBase64;

    public CompleteSigningRequest() {
    }

    public CompleteSigningRequest(String signatureRequestId, String rawSignatureBase64) {
        this.signatureRequestId = signatureRequestId;
        this.rawSignatureBase64 = rawSignatureBase64;
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
}
