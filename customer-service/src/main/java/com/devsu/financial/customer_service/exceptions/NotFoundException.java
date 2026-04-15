package com.devsu.financial.customer_service.exceptions;

/**
 * Custom exception class for handling not found errors.
 * This class extends RuntimeException and provides a constructor
 * to create exceptions with a message.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}