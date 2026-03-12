package com.mymoneylog.server.service.aiReport.llm;

import com.mymoneylog.server.dto.aiReport.AiInsightResponse;
import com.mymoneylog.server.dto.aiReport.MonthlySummaryResponse;

public interface AiClient {
    AiInsightResponse generateMonthlyReport(MonthlySummaryResponse summary);
}