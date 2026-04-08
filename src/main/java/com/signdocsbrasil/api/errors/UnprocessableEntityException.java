package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 422 Unprocessable Entity.
 */
public class UnprocessableEntityException extends ApiException {

    public UnprocessableEntityException(ProblemDetail problemDetail) {
        super(problemDetail);
    }
}
