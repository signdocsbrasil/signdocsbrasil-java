package com.signdocsbrasil.api.errors;

/**
 * Base exception for all SignDocsBrasil SDK errors.
 * Extends {@link RuntimeException} so all SDK exceptions are unchecked.
 */
public class SignDocsBrasilException extends RuntimeException {

    public SignDocsBrasilException(String message) {
        super(message);
    }

    public SignDocsBrasilException(String message, Throwable cause) {
        super(message, cause);
    }
}
