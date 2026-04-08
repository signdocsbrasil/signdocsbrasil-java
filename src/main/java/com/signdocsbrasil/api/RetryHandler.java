package com.signdocsbrasil.api;

import com.signdocsbrasil.api.errors.TimeoutException;

import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements exponential backoff retry logic for retryable HTTP status codes.
 * Retries on 429 (Too Many Requests), 500 (Internal Server Error), and 503 (Service Unavailable).
 * Respects the Retry-After header when present on 429 responses.
 */
final class RetryHandler {

    private static final Set<Integer> RETRYABLE_STATUS_CODES = Set.of(429, 500, 503);
    private static final long MAX_TOTAL_DURATION_MS = 60_000;
    private static final long MAX_DELAY_MS = 30_000;

    private final int maxRetries;

    RetryHandler(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * Determines whether a given HTTP status code is retryable.
     */
    boolean isRetryable(int statusCode) {
        return RETRYABLE_STATUS_CODES.contains(statusCode);
    }

    /**
     * Calculates the delay before the next retry attempt.
     * Uses the Retry-After header if present (for 429 responses), otherwise
     * uses exponential backoff with jitter.
     *
     * @param attempt the zero-based attempt number
     * @param response the HTTP response from the failed request
     * @return the delay in milliseconds
     */
    long calculateDelay(int attempt, HttpResponse<?> response) {
        // Check for Retry-After header
        String retryAfterHeader = response.headers().firstValue("Retry-After").orElse(null);
        if (retryAfterHeader != null) {
            try {
                long retryAfterSeconds = Long.parseLong(retryAfterHeader);
                return retryAfterSeconds * 1000;
            } catch (NumberFormatException ignored) {
                // Fall through to exponential backoff
            }
        }

        // Exponential backoff with jitter
        long baseDelay = (long) Math.pow(2, attempt) * 1000;
        long jitter = ThreadLocalRandom.current().nextLong(1000);
        return Math.min(baseDelay + jitter, MAX_DELAY_MS);
    }

    /**
     * Returns the maximum number of retry attempts.
     */
    int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Returns the maximum total duration allowed for all retry attempts.
     */
    long getMaxTotalDurationMs() {
        return MAX_TOTAL_DURATION_MS;
    }

    /**
     * Sleeps for the given duration, throwing TimeoutException if interrupted.
     */
    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TimeoutException("Retry sleep interrupted");
        }
    }

    /**
     * Checks whether the total elapsed time has exceeded the maximum allowed duration.
     *
     * @param startTimeMs the start time in epoch milliseconds
     * @throws TimeoutException if the maximum duration has been exceeded
     */
    void checkTimeout(long startTimeMs) {
        if (System.currentTimeMillis() - startTimeMs > MAX_TOTAL_DURATION_MS) {
            throw new TimeoutException("Request exceeded maximum retry duration of 60s");
        }
    }
}
