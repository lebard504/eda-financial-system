package com.devsu.financial.customer_service.shared.utils;

public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T payload;
    private Object errors;

    // Constructor
    public ApiResponse(boolean success, int code, String message, T payload, Object errors) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.payload = payload;
        this.errors = errors;
    }

    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getPayload() { return payload; }
    public void setPayload(T payload) { this.payload = payload; }

    public Object getErrors() { return errors; }
    public void setErrors(Object errors) { this.errors = errors; }
}