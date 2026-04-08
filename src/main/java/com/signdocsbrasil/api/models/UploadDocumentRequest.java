package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Request to upload a document to a transaction.
 */
public class UploadDocumentRequest {

    @SerializedName("content")
    private String content;

    @SerializedName("filename")
    private String filename;

    public UploadDocumentRequest() {
    }

    public UploadDocumentRequest(String content) {
        this.content = content;
    }

    public UploadDocumentRequest(String content, String filename) {
        this.content = content;
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
