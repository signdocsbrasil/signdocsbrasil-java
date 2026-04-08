package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 404 Not Found.
 */
public class NotFoundException extends ApiException {

    public NotFoundException(ProblemDetail problemDetail) {
        super(problemDetail);
    }
}
