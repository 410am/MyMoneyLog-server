package com.mymoneylog.server.dto.aiReport;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MonthlySummaryResponse {

    private int year;
    private int month;

    // 조회 범위 (디버깅/확장용)
    private LocalDate rangeStart;
    private LocalDate rangeEndExclusive;

    // 기본 집계
    private long totalExpense;
    private long totalIncome;
    private int transactionCount;
    private long smallPaymentCount;

    // Top 카테고리
    private String topCategory;
    private long topCategoryAmount;
    private int topCategoryPercent;

    // 요일 패턴
    private DayOfWeek topDayOfWeek;

    // Top 3 카테고리 리스트
    private List<CategoryStat> topCategories;
}



