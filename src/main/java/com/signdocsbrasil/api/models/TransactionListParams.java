package com.signdocsbrasil.api.models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query parameters for listing transactions.
 */
public class TransactionListParams {

    private String status;
    private String userExternalId;
    private String documentGroupId;
    private Integer limit;
    private String nextToken;
    private String startDate;
    private String endDate;

    public TransactionListParams() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserExternalId() {
        return userExternalId;
    }

    public void setUserExternalId(String userExternalId) {
        this.userExternalId = userExternalId;
    }

    public String getDocumentGroupId() {
        return documentGroupId;
    }

    public void setDocumentGroupId(String documentGroupId) {
        this.documentGroupId = documentGroupId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Converts the parameters to a query string map suitable for HTTP requests.
     * Null values are excluded.
     */
    public Map<String, String> toQueryMap() {
        Map<String, String> map = new LinkedHashMap<>();
        if (status != null) {
            map.put("status", status);
        }
        if (userExternalId != null) {
            map.put("userExternalId", userExternalId);
        }
        if (documentGroupId != null) {
            map.put("documentGroupId", documentGroupId);
        }
        if (limit != null) {
            map.put("limit", String.valueOf(limit));
        }
        if (nextToken != null) {
            map.put("nextToken", nextToken);
        }
        if (startDate != null) {
            map.put("startDate", startDate);
        }
        if (endDate != null) {
            map.put("endDate", endDate);
        }
        return map;
    }
}
