package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request to generate a presigned URL for document upload.
 */
public class PresignRequest {

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("filename")
    private String filename;

    public PresignRequest() {
    }

    public PresignRequest(String contentType, String filename) {
        this.contentType = contentType;
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
