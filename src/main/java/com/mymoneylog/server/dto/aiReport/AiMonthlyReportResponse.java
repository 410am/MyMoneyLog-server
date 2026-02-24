package com.mymoneylog.server.dto.aiReport;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AiMonthlyReportResponse {
    private String title;          // 예: "2월 소비 리포트"
    private String oneLiner;       // 예: "이번 달은 외식 비중이 높아"
    private List<String> highlights; // 3~5개
    private List<String> tips;       // 3~5개
}