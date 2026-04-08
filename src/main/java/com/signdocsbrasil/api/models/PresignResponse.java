package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response containing a presigned URL for direct document upload.
 */
public class PresignResponse {

    @SerializedName("uploadUrl")
    private String uploadUrl;

    @SerializedName("uploadToken")
    private String uploadToken;

    @SerializedName("s3Key")
    private String s3Key;

    @SerializedName("expiresIn")
    private int expiresIn;

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("instructions")
    private String instructions;

    public PresignResponse() {
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
