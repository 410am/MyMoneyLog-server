package com.mymoneylog.server.repository.record;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mymoneylog.server.dto.dashboard.CategoryChartDto;
import com.mymoneylog.server.dto.dashboard.DailyChartDto;
import com.mymoneylog.server.dto.dashboard.MonthlyChartDto;
import com.mymoneylog.server.dto.dashboard.SummaryDto;
import com.mymoneylog.server.entity.category.QCategory;
import com.mymoneylog.server.entity.record.QRecord;
import com.mymoneylog.server.entity.user.QUser;
import com.mymoneylog.server.enums.IncomeExpenseType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.mymoneylog.server.entity.record.Record;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecordRepositoryImpl implements RecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QRecord r = QRecord.record;

    @Override
    public Optional<Record> findWithUserAndCategoryById(Long recordId) {
        QUser user = QUser.user;
        QCategory category = QCategory.category;

        return Optional.ofNullable(queryFactory
                .selectFrom(r)
                .join(r.user, user).fetchJoin()
                .join(r.category, category).fetchJoin()
                .where(r.recordId.eq(recordId))
                .fetchOne());
    }

    @Override
    public SummaryDto findMonthlySummary(Long userId, LocalDate start, LocalDate end) {
        NumberExpression<Long> totalIncomeExpr = new CaseBuilder()
                .when(r.type.eq(IncomeExpenseType.INCOME))
                .then(r.amount.longValue())
                .otherwise(0L)
                .sum()
                .coalesce(0L);

        NumberExpression<Long> totalExpenseExpr = new CaseBuilder()
                .when(r.type.eq(IncomeExpenseType.EXPENSE))
                .then(r.amount.longValue())
                .otherwise(0L)
                .sum()
                .coalesce(0L);

        NumberExpression<Long> incomeCountExpr = new CaseBuilder()
                .when(r.type.eq(IncomeExpenseType.INCOME))
                .then(1L)
                .otherwise(0L)
                .sum()
                .coalesce(0L);

        NumberExpression<Long> expenseCountExpr = new CaseBuilder()
                .when(r.type.eq(IncomeExpenseType.EXPENSE))
                .then(1L)
                .otherwise(0L)
                .sum()
                .coalesce(0L);

        return queryFactory
                .select(
                        Projections.constructor(
                                SummaryDto.class,
                                // 🔹 총 수입
                                totalIncomeExpr,
                                // 🔹 총 지출
                                totalExpenseExpr,
                                // 🔹 수입 건수
                                incomeCountExpr,
                                // 🔹 지출 건수
                                expenseCountExpr
                        )
                )
                .from(r)
                .where(
                        r.user.userId.eq(userId),
                        r.date.goe(start),
                        r.date.lt(end)
                )
                .fetchOne();
    }

    @Override
    public List<CategoryChartDto> findCategoryStats(Long userId, LocalDate start, LocalDate end) {
        return queryFactory
                .select(
                        Projections.constructor(
                                CategoryChartDto.class,
                                r.category.categoryId,
                                r.category.name,
                                r.amount.sum()
                        )
                )
                .from(r)
                .where(
                        r.user.userId.eq(userId),
                        r.type.eq(IncomeExpenseType.EXPENSE),
                        r.date.goe(start),
                        r.date.lt(end)   // between(start, end.minusDays(1)) 보다 이게 정확
                )
                .groupBy(r.category.categoryId, r.category.name)
                .orderBy(r.amount.sum().desc())
                .fetch();
    }

    @Override
    public List<DailyChartDto> findDailyStats(Long userId, LocalDate start, LocalDate end) {
        return queryFactory
                .select(
                        Projections.constructor(
                                DailyChartDto.class,
                                r.date,
                                r.amount.sum()
                        )
                )
                .from(r)
                .where(
                        r.user.userId.eq(userId),
                        r.type.eq(IncomeExpenseType.EXPENSE),
                        r.date.goe(start),
                        r.date.lt(end)
                )
                .groupBy(r.date)
                .orderBy(r.date.asc())
                .fetch();
    }
    

    @Override
    public List<MonthlyChartDto> findMonthlyStats(Long userId, int year) {
        return queryFactory
                .select(
                        Projections.constructor(
                                MonthlyChartDto.class,
                                r.date.month(),
                                r.amount.sum()
                        )
                )
                .from(r)
                .where(
                        r.user.userId.eq(userId),
                        r.type.eq(IncomeExpenseType.EXPENSE),
                        r.date.year().eq(year)
                )
                .groupBy(r.date.month())
                .orderBy(r.date.month().asc())
                .fetch();
    }
}
