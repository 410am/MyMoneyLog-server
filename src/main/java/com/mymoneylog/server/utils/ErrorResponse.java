package com.mymoneylog.server.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * advice 및 전역 에러 처리 response
 */
@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> errors;  // DTO 에서 설정된 에러 메시지들이 담김
}