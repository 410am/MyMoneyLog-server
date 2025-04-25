package com.mymoneylog.server.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mymoneylog.server.utils.ApiResponse;
import com.mymoneylog.server.utils.CommonConstants;
import com.mymoneylog.server.utils.ErrorResponse;
import com.mymoneylog.server.utils.CustomException;

import java.util.HashMap;
import java.util.Map;

/**
 * 스프링 흐름도 (참고!)
 * 전역 에러 처리 및 valid 오류 응답 처리 advice
 *
 * 요청 순서
 * - Client → [Filter] → [Interceptor: preHandle()] → [Controller] → [@RestControllerAdvice] (예외 발생 시)
 * 응답 순서
 * - [@RestControllerAdvice] (예외 발생 시) → [Controller] → [Interceptor: postHandle()] → [Interceptor: afterCompletion()] → [Filter] → Client
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 유효성 검증 실패 시 처리 (400 에러)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())  // DTO에서 설정한 에러 메시지
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(CommonConstants.GLOBAL_VALID_MSG)
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 서비스에서 에러처리 하지 않은 모든 예외 처리 (500 에러)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(CommonConstants.GLOBAL_ERROR_MSG)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // 커스컴 에러 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(ex.getStatus())
                .message(ex.getMessage())
                .errors(null)
                .build();

        return ResponseEntity.status(ex.getStatus()).body(error);
    }
}
