package com.mymoneylog.server.dto.dashboard;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyChartDto {
    private Integer month;        // 1 ~ 12 
    private Integer amount;      // 그 달 지출 합(원 단위) 
}
