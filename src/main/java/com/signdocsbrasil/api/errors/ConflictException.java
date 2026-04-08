package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 409 Conflict.
 */
public class ConflictException extends ApiException {

    public ConflictException(ProblemDetail problemDetail) {
        super(problemDetail);
    }
}
