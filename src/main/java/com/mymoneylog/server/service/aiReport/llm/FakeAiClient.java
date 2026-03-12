// package com.mymoneylog.server.service.aiReport.llm;


// import com.mymoneylog.server.dto.aiReport.AiMonthlyReportResponse;
// import com.mymoneylog.server.dto.aiReport.MonthlySummaryResponse;
// import org.springframework.context.annotation.Primary;
// import org.springframework.stereotype.Component;

// import java.util.List;

// @Component
// // @Primary // 일단 이걸로 붙여놓고, OpenAI 붙이면 @Primary 떼면 됨
// public class FakeAiClient implements AiClient {
//     @Override
//     public AiMonthlyReportResponse generateMonthlyReport(MonthlySummaryResponse summary) {
//         return AiMonthlyReportResponse.builder()
//                 .title(summary.getMonth() + "월 소비 리포트")
//                 .oneLiner("이번 달 데이터가 쌓이면 패턴을 더 잘 보여줄 수 있어.")
//                 .highlights(List.of(
//                         "총 지출: " + summary.getTotalExpense() + "원",
//                         "총 수입: " + summary.getTotalIncome() + "원",
//                         "Top 카테고리: " + (summary.getTopCategory() == null ? "없음" : summary.getTopCategory())
//                 ))
//                 .tips(List.of(
//                         "한 달치 데이터가 최소 10건 이상 쌓아보세요.",
//                         "카테고리를 '미분류' 없이 넣어두면 분석이 더 좋아져요",
//                         "지출 메모를 남겨보세요."
//                 ))
//                 .build();
//     }
// }