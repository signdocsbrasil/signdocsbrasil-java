package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.HealthCheckResponse;
import com.signdocsbrasil.api.models.HealthHistoryResponse;

import java.time.Duration;

/**
 * Resource for health check operations.
 * Health endpoints do not require authentication.
 */
public final class HealthResource {

    private final HttpClient http;

    public HealthResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Checks the current health status of the API.
     *
     * @return the health check response
     */
    public HealthCheckResponse check() {
        return http.requestNoAuth("GET", "/health", HealthCheckResponse.class);
    }

    /**
     * Checks the current health status of the API with a per-request timeout.
     *
     * @param timeout the request timeout
     * @return the health check response
     */
    public HealthCheckResponse check(Duration timeout) {
        return http.requestNoAuth("GET", "/health", HealthCheckResponse.class, timeout);
    }

    /**
     * Retrieves the health check history.
     *
     * @return the health history response
     */
    public HealthHistoryResponse history() {
        return http.requestNoAuth("GET", "/health/history", HealthHistoryResponse.class);
    }

    /**
     * Retrieves the health check history with a per-request timeout.
     *
     * @param timeout the request timeout
     * @return the health history response
     */
    public HealthHistoryResponse history(Duration timeout) {
        return http.requestNoAuth("GET", "/health/history", HealthHistoryResponse.class, timeout);
    }
}
