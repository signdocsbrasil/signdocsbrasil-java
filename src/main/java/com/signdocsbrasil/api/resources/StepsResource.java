package com.signdocsbrasil.api.resources;

import com.signdocsbrasil.api.HttpClient;
import com.signdocsbrasil.api.models.StartStepRequest;
import com.signdocsbrasil.api.models.StartStepResponse;
import com.signdocsbrasil.api.models.StepCompleteResponse;
import com.signdocsbrasil.api.models.StepListResponse;

import java.time.Duration;
import java.util.Map;

/**
 * Resource for step operations within a transaction.
 */
public final class StepsResource {

    private final HttpClient http;

    public StepsResource(HttpClient http) {
        this.http = http;
    }

    /**
     * Lists all steps for a transaction.
     *
     * @param transactionId the transaction ID
     * @return the step list response
     */
    public StepListResponse list(String transactionId) {
        return http.request("GET", "/v1/transactions/" + transactionId + "/steps",
                null, StepListResponse.class);
    }

    /**
     * Lists all steps for a transaction with a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param timeout       the request timeout
     * @return the step list response
     */
    public StepListResponse list(String transactionId, Duration timeout) {
        return http.request("GET", "/v1/transactions/" + transactionId + "/steps",
                null, StepListResponse.class, timeout);
    }

    /**
     * Starts a step with default options.
     *
     * @param transactionId the transaction ID
     * @param stepId        the step ID
     * @return the start step response
     */
    public StartStepResponse start(String transactionId, String stepId) {
        return start(transactionId, stepId, null);
    }

    /**
     * Starts a step with the given request options.
     *
     * @param transactionId the transaction ID
     * @param stepId        the step ID
     * @param request       the start step request (e.g., capture mode, OTP channel), or null
     * @return the start step response
     */
    public StartStepResponse start(String transactionId, String stepId, StartStepRequest request) {
        Object body = request != null ? request : Map.of();
        return http.request("POST",
                "/v1/transactions/" + transactionId + "/steps/" + stepId + "/start",
                body, StartStepResponse.class);
    }

    /**
     * Starts a step with the given request options and a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param stepId        the step ID
     * @param request       the start step request (e.g., capture mode, OTP channel), or null
     * @param timeout       the request timeout
     * @return the start step response
     */
    public StartStepResponse start(String transactionId, String stepId, StartStepRequest request, Duration timeout) {
        Object body = request != null ? request : Map.of();
        return http.request("POST",
                "/v1/transactions/" + transactionId + "/steps/" + stepId + "/start",
                body, StartStepResponse.class, timeout);
    }

    /**
     * Completes a step with default options (empty body).
     *
     * @param transactionId the transaction ID
     * @param stepId        the step ID
     * @return the step complete response
     */
    public StepCompleteResponse complete(String transactionId, String stepId) {
        return complete(transactionId, stepId, null);
    }

    /**
     * Completes a step with the given request body.
     * The request body type depends on the step type (click, OTP, liveness, biometric match).
     *
     * @param transactionId the transaction ID
     * @param stepId        the step ID
     * @param request       the complete step request body, or null for empty body
     * @return the step complete response
     */
    public StepCompleteResponse complete(String transactionId, String stepId, Object request) {
        Object body = request != null ? request : Map.of();
        return http.request("POST",
                "/v1/transactions/" + transactionId + "/steps/" + stepId + "/complete",
                body, StepCompleteResponse.class);
    }

    /**
     * Completes a step with the given request body and a per-request timeout.
     *
     * @param transactionId the transaction ID
     * @param stepId        the step ID
     * @param request       the complete step request body, or null for empty body
     * @param timeout       the request timeout
     * @return the step complete response
     */
    public StepCompleteResponse complete(String transactionId, String stepId, Object request, Duration timeout) {
        Object body = request != null ? request : Map.of();
        return http.request("POST",
                "/v1/transactions/" + transactionId + "/steps/" + stepId + "/complete",
                body, StepCompleteResponse.class, timeout);
    }
}
