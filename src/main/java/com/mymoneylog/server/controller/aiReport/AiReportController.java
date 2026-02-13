package com.mymoneylog.server.controller.aiReport;


import com.mymoneylog.server.service.aiReport.AiReportService;
import com.mymoneylog.server.dto.aiReport.MonthlySummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-reports")
public class AiReportController {

    private final AiReportService aiReportService;

    @GetMapping("/current/summary")
    public MonthlySummaryResponse getCurrentMonthSummary(
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = extractUserId(principal);
        return aiReportService.getCurrentMonthSummary(userId);
    }

    private Long extractUserId(Object principal) {
        if (principal == null) {
            throw new IllegalStateException("principal is null (not authenticated)");
        }
        if (principal instanceof Long l) return l;
        if (principal instanceof Integer i) return i.longValue();
        if (principal instanceof String s) return Long.parseLong(s);

        
        try {
            var m = principal.getClass().getMethod("getUserId");
            Object v = m.invoke(principal);
            if (v instanceof Long l) return l;
            if (v instanceof Integer i) return i.longValue();
            return Long.parseLong(v.toString());
        } catch (Exception e) {
            throw new IllegalStateException("Cannot extract userId from principal type: "
                    + principal.getClass().getName(), e);
        }
    }
}
