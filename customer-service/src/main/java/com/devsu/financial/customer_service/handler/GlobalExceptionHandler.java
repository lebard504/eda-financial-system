package com.devsu.financial.customer_service.handler;

import com.devsu.financial.customer_service.shared.utils.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.devsu.financial.customer_service.exceptions.*;

import java.util.regex.*;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // 400 - VALIDATION
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return ResponseEntity
                .badRequest()
                .body(ResponseBuilder.error(400, "Validation failed", errors));
    }

    // =========================
    // 400 - BAD BODY FORMAT
    // =========================
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(ResponseBuilder.error(
                        400,
                        "Invalid request body: " + ex.getMostSpecificCause().getMessage(),
                        null
                ));
    }

    // =========================
    // 404 - NOT FOUND
    // =========================
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFound404(NoHandlerFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ResponseBuilder.error(
                        404,
                        "Endpoint not found: " + ex.getRequestURL(),
                        null
                ));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ResponseBuilder.error(404, ex.getMessage(), null));
    }

    // =========================
    // 409 - DUPLICATE (SPRING REAL ERROR)
    // =========================
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String message = ex.getMostSpecificCause().getMessage();
        String field = "unique field";

        if (message != null && message.contains("IDENTIFICATION")) {
            field = "identification";
        }

        return ResponseEntity
                .status(409)
                .body(ResponseBuilder.error(
                        409,
                        "Duplicate value for field: " + field,
                        null
                ));
    }

    // Opcional (por si usas Mongo u otros casos)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<?> handleDuplicateKey(DuplicateKeyException ex) {
        return ResponseEntity
                .status(409)
                .body(ResponseBuilder.error(
                        409,
                        "Duplicate key error",
                        null
                ));
    }

    // =========================
    // 401 - AUTH
    // =========================
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity
                .status(401)
                .body(ResponseBuilder.error(401, ex.getMessage(), null));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity
                .status(401)
                .body(ResponseBuilder.error(401, ex.getMessage(), null));
    }

    // =========================
    // 400 - BUSINESS RULES
    // =========================
    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<?> handleInvalidTransition(InvalidStateTransitionException ex) {
        return ResponseEntity
                .badRequest()
                .body(ResponseBuilder.error(400, ex.getMessage(), null));
    }

    // =========================
    // 405 - METHOD NOT ALLOWED
    // =========================
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity
                .status(405)
                .body(ResponseBuilder.error(
                        405,
                        "HTTP method not supported: " + ex.getMethod(),
                        null
                ));
    }

    // =========================
    // 500 - APP LEVEL
    // =========================
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleAppLevel(ApplicationException ex) {
        return ResponseEntity
                .status(500)
                .body(ResponseBuilder.error(
                        500,
                        "Application error: " + ex.getMessage(),
                        null
                ));
    }

    // =========================
    // 500 - UNKNOWN
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknown(Exception ex) {
        return ResponseEntity
                .status(500)
                .body(ResponseBuilder.error(
                        500,
                        "Unexpected error: " + ex.getMessage(),
                        null
                ));
    }
}