package com.mymoneylog.server.dto.dashboard;

import java.util.List;

import lombok.*;

@Getter
@Builder
public class DashboardResDTO {

    private SummaryDto summary;                       // 상단 카드용 
    private List<CategoryChartDto> categoryChart;     // 도넛 차트용 
    private List<DailyChartDto> dailyChart;           // 일별 라인 그래프용 
    private List<MonthlyChartDto> monthlyChart;       // 월별 막대 그래프용 

}
