package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request to confirm a document upload after using presigned URL.
 */
public class ConfirmDocumentRequest {

    @SerializedName("uploadToken")
    private String uploadToken;

    public ConfirmDocumentRequest() {
    }

    public ConfirmDocumentRequest(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }
}
