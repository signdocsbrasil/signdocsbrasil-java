package com.signdocsbrasil.api.errors;

/**
 * Thrown when OAuth2 token acquisition fails.
 */
public class AuthenticationException extends SignDocsBrasilException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
