package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.CombinedStampResponse;

import java.time.Duration;

/**
 * Resource for document group operations.
 */
public final class DocumentGroupsResource {

    private final HttpClient http;

    public DocumentGroupsResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Generates a combined stamp for all documents in a group.
     *
     * @param documentGroupId the document group ID
     * @return the combined stamp response with stamped document URL
     */
    public CombinedStampResponse combinedStamp(String documentGroupId) {
        return http.request("POST",
                "/v1/document-groups/" + documentGroupId + "/combined-stamp",
                null, CombinedStampResponse.class);
    }

    /**
     * Generates a combined stamp for all documents in a group with a per-request timeout.
     *
     * @param documentGroupId the document group ID
     * @param timeout         the request timeout
     * @return the combined stamp response with stamped document URL
     */
    public CombinedStampResponse combinedStamp(String documentGroupId, Duration timeout) {
        return http.request("POST",
                "/v1/document-groups/" + documentGroupId + "/combined-stamp",
                null, CombinedStampResponse.class, timeout);
    }
}
