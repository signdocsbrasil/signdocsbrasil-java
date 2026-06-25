package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response from {@code POST /v1/verify/document}, reporting whether a PDF
 * carries any detectable signatures and describing each one.
 */
public class VerifyDocumentResponse {

    @SerializedName("signed")
    private boolean signed;

    @SerializedName("signatureCount")
    private int signatureCount;

    @SerializedName("signatures")
    private List<DetectedSignature> signatures;

    @SerializedName("checkedAt")
    private String checkedAt;

    public VerifyDocumentResponse() {
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public int getSignatureCount() {
        return signatureCount;
    }

    public void setSignatureCount(int signatureCount) {
        this.signatureCount = signatureCount;
    }

    public List<DetectedSignature> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<DetectedSignature> signatures) {
        this.signatures = signatures;
    }

    public String getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(String checkedAt) {
        this.checkedAt = checkedAt;
    }
}
