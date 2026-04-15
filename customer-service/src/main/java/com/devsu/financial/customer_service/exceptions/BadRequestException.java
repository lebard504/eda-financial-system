package com.devsu.financial.customer_service.exceptions;
/**
 * Custom exception class for handling bad requests.
 * This class extends RuntimeException and provides a constructor
 * to create exceptions with a message.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
