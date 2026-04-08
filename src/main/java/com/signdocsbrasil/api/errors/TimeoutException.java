package com.signdocsbrasil.api.errors;

/**
 * Thrown when a request times out or maximum retry duration is exceeded.
 */
public class TimeoutException extends SignDocsBrasilException {

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
