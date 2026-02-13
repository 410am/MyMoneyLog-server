package com.mymoneylog.server.dto.aiReport;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryStat {
    private String categoryName;
    private long amount;
    private int percent;
}