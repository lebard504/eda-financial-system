package com.devsu.financial.account_service.exceptions;
/**
 * Custom exception class for handling invalid token errors.
 * This class extends RuntimeException and provides a constructor
 * to create exceptions with a message.
 */
public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String message) {
        super(message);
    }
}