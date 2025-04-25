package com.mymoneylog.server.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseEntity<T> extends ResponseEntity<ApiResponse<T>> {

    public ApiResponseEntity(HttpStatus status, String message, T data) {
        super(new ApiResponse<>(status.value(), message, data), status);
    }

    public static <T> ApiResponseEntity<T> ok(String message, T data) {
        String responseMessage = message == null ? CommonConstants.GLOBAL_SUCCESS_MSG : message;
        return new ApiResponseEntity<>(HttpStatus.OK, responseMessage, data);
    }

    public static ApiResponseEntity<Void> error(HttpStatus status, String message) {
        String responseMessage = message == null ? CommonConstants.GLOBAL_ERROR_MSG : message;
        return new ApiResponseEntity<>(status, responseMessage, null);
    }
}