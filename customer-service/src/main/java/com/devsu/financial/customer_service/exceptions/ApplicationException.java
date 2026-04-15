package com.devsu.financial.customer_service.exceptions;
/**
 * Custom exception class for application-specific errors.
 * This class extends RuntimeException and provides constructors
 * to create exceptions with a message and/or a cause.
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
