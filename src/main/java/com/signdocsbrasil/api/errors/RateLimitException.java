package com.signdocsbrasil.api.errors;

/**
 * Thrown when the API returns HTTP 429 Too Many Requests.
 * Contains the optional Retry-After value in seconds.
 */
public class RateLimitException extends ApiException {

    private final Integer retryAfterSeconds;

    public RateLimitException(ProblemDetail problemDetail, Integer retryAfterSeconds) {
        super(problemDetail);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * Returns the number of seconds to wait before retrying, or null if not specified.
     */
    public Integer getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
