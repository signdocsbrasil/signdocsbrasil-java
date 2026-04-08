package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response containing health check history entries.
 */
public class HealthHistoryResponse {

    @SerializedName("entries")
    private List<HealthCheckResponse> entries;

    public HealthHistoryResponse() {
    }

    public List<HealthCheckResponse> getEntries() {
        return entries;
    }

    public void setEntries(List<HealthCheckResponse> entries) {
        this.entries = entries;
    }
}
