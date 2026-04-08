package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 500 Internal Server Error.
 */
public class InternalServerException extends ApiException {

    public InternalServerException(ProblemDetail problemDetail) {
        super(problemDetail);
    }
}
