package com.signdocsbrasil.api.errors;

/**
 * Thrown when a network connection to the API cannot be established.
 */
public class ConnectionException extends SignDocsBrasilException {

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
