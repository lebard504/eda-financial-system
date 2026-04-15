package com.devsu.financial.account_service.exceptions;
/**
 * Custom exception class for handling unauthorized access errors.
 * This class extends RuntimeException and provides a constructor
 * to create exceptions with a message.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
