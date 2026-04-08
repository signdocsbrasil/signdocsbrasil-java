package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 503 Service Unavailable.
 */
public class ServiceUnavailableException extends ApiException {

    public ServiceUnavailableException(ProblemDetail problemDetail) {
        super(problemDetail);
    }
}
