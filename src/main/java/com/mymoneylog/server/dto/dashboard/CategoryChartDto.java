package com.mymoneylog.server.dto.dashboard;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryChartDto {
    private Long categoryId;
    private String categoryName;
    private Integer amount;    // 해당 카테고리 지출 총합 (원 단위)
}
