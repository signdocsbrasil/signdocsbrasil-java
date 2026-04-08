package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Response from generating a combined stamp for a document group.
 */
public class CombinedStampResponse {

    @SerializedName("groupId")
    private String groupId;

    @SerializedName("signerCount")
    private int signerCount;

    @SerializedName("downloadUrl")
    private String downloadUrl;

    @SerializedName("expiresIn")
    private int expiresIn;

    public CombinedStampResponse() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getSignerCount() {
        return signerCount;
    }

    public void setSignerCount(int signerCount) {
        this.signerCount = signerCount;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
