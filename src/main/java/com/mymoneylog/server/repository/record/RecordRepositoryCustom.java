package com.mymoneylog.server.repository.record;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mymoneylog.server.dto.dashboard.CategoryChartDto;
import com.mymoneylog.server.dto.dashboard.DailyChartDto;
import com.mymoneylog.server.dto.dashboard.MonthlyChartDto;
import com.mymoneylog.server.dto.dashboard.SummaryDto;
import com.mymoneylog.server.entity.record.Record;

public interface RecordRepositoryCustom {
    Optional<Record> findWithUserAndCategoryById(Long recordId);
    SummaryDto findMonthlySummary(Long userId, LocalDate start, LocalDate end);
    List<CategoryChartDto> findCategoryStats(Long userId, LocalDate start, LocalDate end);
    List<DailyChartDto> findDailyStats(Long userId, LocalDate start, LocalDate end);
    List<MonthlyChartDto> findMonthlyStats(Long userId, int year);
}
