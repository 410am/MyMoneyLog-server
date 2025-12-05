package com.mymoneylog.server.dto.dashboard;

import java.time.LocalDate;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyChartDto {
    private LocalDate date;   // 2025-12-01 이런 형식 
    private Integer amount;      // 해당 날짜 지출 총합 (원 단위) 
   
}
