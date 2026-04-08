package com.signdocsbrasil.api.errors;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * RFC 7807 Problem Details representation.
 * Returned by the API for all error responses.
 */
public final class ProblemDetail {

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("status")
    private int status;

    @SerializedName("detail")
    private String detail;

    @SerializedName("instance")
    private String instance;

    /** Additional extension fields not mapped to specific properties. */
    private transient Map<String, Object> extensions;

    public ProblemDetail() {
    }

    public ProblemDetail(String type, String title, int status, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }

    /**
     * Creates a fallback ProblemDetail when the response body is not RFC 7807 format.
     */
    public static ProblemDetail fallback(int status, String body) {
        return new ProblemDetail(
                "https://api.signdocs.com.br/errors/" + status,
                "HTTP " + status,
                status,
                body,
                null
        );
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        return "ProblemDetail{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", detail='" + detail + '\'' +
                ", instance='" + instance + '\'' +
                '}';
    }
}
