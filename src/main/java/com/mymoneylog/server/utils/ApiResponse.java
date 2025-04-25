package com.mymoneylog.server.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * api response
 *
 */
@Getter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int status;      // 상태 코드 (예: 200, 400, 500)
    private String message;   // 응답 메시지 (예: 성공, 에러 메시지 등)
    private T data;           // 실제 데이터
}