package com.mymoneylog.server.service.aiReport.llm;

import com.mymoneylog.server.dto.aiReport.AiMonthlyReportResponse;
import com.mymoneylog.server.dto.aiReport.MonthlySummaryResponse;

public interface AiClient {
    AiMonthlyReportResponse generateMonthlyReport(MonthlySummaryResponse summary);
}