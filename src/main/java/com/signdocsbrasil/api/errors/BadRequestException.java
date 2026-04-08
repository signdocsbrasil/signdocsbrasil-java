package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 400 Bad Request.
 */
public class BadRequestException extends ApiException {

    public BadRequestException(ProblemDetail problemDetail) {
        super(problemDetail);
    }
}
