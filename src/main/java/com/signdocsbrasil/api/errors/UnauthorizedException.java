package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 401 Unauthorized.
 */
public class UnauthorizedException extends ApiException {

    public UnauthorizedException(ProblemDetail problemDetail) {
        super(problemDetail);
    }
}
