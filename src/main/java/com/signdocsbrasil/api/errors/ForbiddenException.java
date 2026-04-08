package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 403 Forbidden.
 */
public class ForbiddenException extends ApiException {

    public ForbiddenException(ProblemDetail problemDetail) {
        super(problemDetail);
    }
}
