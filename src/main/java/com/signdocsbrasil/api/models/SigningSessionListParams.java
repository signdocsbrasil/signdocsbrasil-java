package com.signdocsbrasil.api.models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Query parameters for listing signing sessions.
 */
public class SigningSessionListParams {

    private String status;
    private Integer limit;
    private String nextToken;
    private String startDate;
    private String endDate;

    public SigningSessionListParams() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
