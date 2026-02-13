package com.mymoneylog.server.service.aiReport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mymoneylog.server.entity.record.Record;
import com.mymoneylog.server.enums.IncomeExpenseType;
import com.mymoneylog.server.dto.aiReport.CategoryStat;
import com.mymoneylog.server.dto.aiReport.MonthlySummaryResponse;
import com.mymoneylog.server.repository.record.RecordRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiReportService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final long SMALL_PAYMENT_THRESHOLD = 50_000L;

    private final RecordRepository recordRepository;

    public MonthlySummaryResponse getCurrentMonthSummary(Long userId) {
        YearMonth ym = YearMonth.now(KST);
        LocalDate start = ym.atDay(1);
        LocalDate endExclusive = ym.plusMonths(1).atDay(1);

        List<Record> records = recordRepository.findByUser_UserIdAndDateGreaterThanEqualAndDateLessThan(
            userId,
            start,
            endExclusive
        );

        long totalExpense = records.stream()
                .filter(r -> r.getType() == IncomeExpenseType.EXPENSE)
                .mapToLong(r -> toLong(r.getAmount()))
                .sum();

        long totalIncome = records.stream()
                .filter(r -> r.getType() == IncomeExpenseType.INCOME)
                .mapToLong(r -> toLong(r.getAmount()))
                .sum();

        int transactionCount = records.size();

        long smallPaymentCount = records.stream()
                .filter(r -> r.getType() == IncomeExpenseType.EXPENSE)
                .filter(r -> toLong(r.getAmount()) > 0 && toLong(r.getAmount()) <= SMALL_PAYMENT_THRESHOLD)
                .count();

        // 카테고리별 지출 합계 (expense만)
        Map<String, Long> expenseByCategory = records.stream()
                .filter(r -> r.getType() == IncomeExpenseType.EXPENSE)
                .collect(Collectors.groupingBy(
                        r -> safeCategoryName(r),
                        Collectors.summingLong(r -> toLong(r.getAmount()))
                ));

        // top category
        Optional<Map.Entry<String, Long>> topCategoryEntry = expenseByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        String topCategory = topCategoryEntry.map(Map.Entry::getKey).orElse(null);
        Long topCategoryAmount = topCategoryEntry.map(Map.Entry::getValue).orElse(0L);

        int topCategoryPercent = (totalExpense > 0)
                ? (int) Math.round((topCategoryAmount * 100.0) / totalExpense)
                : 0;

        // 요일별 지출 건수 (원하면 sum으로 바꿔도 됨)
        Map<DayOfWeek, Long> expenseCountByDayOfWeek = records.stream()
                .filter(r -> r.getType() == IncomeExpenseType.EXPENSE)
                .collect(Collectors.groupingBy(
                        r -> r.getDate().getDayOfWeek(),
                        Collectors.counting()
                ));

        DayOfWeek topDayOfWeek = expenseCountByDayOfWeek.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // 카테고리 Top3 (화면/AI 입력에 쓰기 좋음)
        List<CategoryStat> topCategories = expenseByCategory.entrySet().stream()
        .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
        .limit(3)
        .map(e -> CategoryStat.builder()
                .categoryName(e.getKey())
                .amount(e.getValue())
                .percent(totalExpense > 0 ? (int) Math.round((e.getValue() * 100.0) / totalExpense) : 0)
                .build())
        .collect(Collectors.toList());

        return MonthlySummaryResponse.builder()
                .year(ym.getYear())
                .month(ym.getMonthValue())
                .rangeStart(start)
                .rangeEndExclusive(endExclusive)
                .totalExpense(totalExpense)
                .totalIncome(totalIncome)
                .transactionCount(transactionCount)
                .smallPaymentCount(smallPaymentCount)
                .topCategory(topCategory)
                .topCategoryAmount(topCategoryAmount)
                .topCategoryPercent(topCategoryPercent)
                .topDayOfWeek(topDayOfWeek)
                .topCategories(topCategories)
                .build();
    }

    private static String safeCategoryName(Record r) {
        if (r.getCategory() == null) return "미분류";
        String name = r.getCategory().getName();
        return (name == null || name.isBlank()) ? "미분류" : name;
    }

    private static long toLong(Object amount) {
        // amount 타입이 long/int/Integer/Long 섞여도 안전하게
        if (amount == null) return 0L;
        if (amount instanceof Number n) return n.longValue();
        return Long.parseLong(amount.toString());
    }
}
