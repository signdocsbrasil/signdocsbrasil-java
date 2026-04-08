package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Request to prepare a digital signing operation.
 */
public class PrepareSigningRequest {

    @SerializedName("certificateChainPems")
    private List<String> certificateChainPems;

    public PrepareSigningRequest() {
    }

    public PrepareSigningRequest(List<String> certificateChainPems) {
        this.certificateChainPems = certificateChainPems;
    }

    public List<String> getCertificateChainPems() {
        return certificateChainPems;
    }

    public void setCertificateChainPems(List<String> certificateChainPems) {
        this.certificateChainPems = certificateChainPems;
    }
}
