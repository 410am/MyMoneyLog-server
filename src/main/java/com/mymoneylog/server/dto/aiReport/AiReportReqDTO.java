package com.mymoneylog.server.dto.aiReport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiReportReqDTO {
    private Long userId;
    private String content;
    private String month; // "2025-04"
}
