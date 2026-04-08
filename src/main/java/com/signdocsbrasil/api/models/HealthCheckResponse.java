package com.signdocsbrasil.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * Response from the health check endpoint.
 */
public class HealthCheckResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("version")
    private String version;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("services")
    private Map<String, ServiceStatus> services;

    public HealthCheckResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, ServiceStatus> getServices() {
        return services;
    }

    public void setServices(Map<String, ServiceStatus> services) {
        this.services = services;
    }

    /**
     * Status of an individual service dependency.
     */
    public static class ServiceStatus {
        @SerializedName("status")
        private String status;

        @SerializedName("latency")
        private Double latency;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Double getLatency() {
            return latency;
        }

        public void setLatency(Double latency) {
            this.latency = latency;
        }
    }
}
