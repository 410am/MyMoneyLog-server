package com.mymoneylog.server.service.dashboard;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mymoneylog.server.dto.dashboard.CategoryChartDto;
import com.mymoneylog.server.dto.dashboard.DailyChartDto;
import com.mymoneylog.server.dto.dashboard.DashboardResDTO;
import com.mymoneylog.server.dto.dashboard.MonthlyChartDto;
import com.mymoneylog.server.dto.dashboard.SummaryDto;
import com.mymoneylog.server.repository.record.RecordRepository;

import io.opencensus.metrics.export.Summary;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RecordRepository recordRepository;

    public DashboardResDTO getDashboard(Long userId, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.plusMonths(1).atDay(1);

        SummaryDto summary = recordRepository.findMonthlySummary(userId, start, end);
        List<CategoryChartDto> categoryChart = recordRepository.findCategoryStats(userId, start, end);
        List<DailyChartDto> dailyChart = recordRepository.findDailyStats(userId, start, end);
        List<MonthlyChartDto> monthlyChart = recordRepository.findMonthlyStats(userId, year);

         return DashboardResDTO.builder()
                .summary(summary)
                .categoryChart(categoryChart)
                .dailyChart(dailyChart)
                .monthlyChart(monthlyChart)
                .build();
    }
}
