package com.devsu.financial.account_service.shared.utils;

public class ResponseBuilder {

    public static <T> ApiResponse<T> success(int httpCode, String message, T data) {
        return new ApiResponse<>(true, httpCode, message, data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, 200, message, data, null);
    }

    public static <T> ApiResponse<T> error(int httpCode, String message, T data, Object errors) {
        return new ApiResponse<>(false, httpCode, message, data, errors);
    }

    public static ApiResponse<Object> error(int httpCode, String message, Object errors) {
        return new ApiResponse<>(false, httpCode, message, null, errors);
    }

    public static ApiResponse<Object> error(String message) {
        return new ApiResponse<>(false, 500, message, null, null);
    }
}