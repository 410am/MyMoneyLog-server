package com.mymoneylog.server.controller.aiReport;

import com.mymoneylog.server.dto.aiReport.AiReportReqDTO;
import com.mymoneylog.server.entity.aiReport.AiReport;
import com.mymoneylog.server.service.aiReport.AiReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai-report")
@RequiredArgsConstructor
public class AiReportController {

    private final AiReportService aiReportService;

    @PostMapping
    public AiReport createReport(@RequestBody AiReportReqDTO dto) {
        return aiReportService.saveReport(dto);
    }
}
