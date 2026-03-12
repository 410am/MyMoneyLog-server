package com.mymoneylog.server.service.aiReport.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mymoneylog.server.dto.aiReport.AiInsightResponse;
import com.mymoneylog.server.dto.aiReport.MonthlySummaryResponse;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class OpenAiClientImpl implements AiClient {

    private final OpenAIClient client;
    private final ObjectMapper objectMapper;

    @Override
    public AiInsightResponse generateMonthlyReport(MonthlySummaryResponse summary) {
        try {
            String summaryJson = objectMapper.writeValueAsString(summary);

            String prompt = """
                    너는 가계부 앱의 소비 분석 코치야.
                    사용자는 이미 숫자와 그래프를 보고 있으니,
                    아래 데이터를 바탕으로 반드시 JSON만 반환해.

                    형식:
                    {
                      "headline": "한국어 한 문장",
                      "tips": [
                        "짧은 팁 1",
                        "짧은 팁 2",
                        "짧은 팁 3"
                      ]
                    }

                    규칙:
                    - headline은 1문장만
                    - tips는 정확히 3개
                    - 존댓말 쓰지 마
                    - JSON 외의 텍스트 절대 금지
                    반드시 순수 JSON만 반환해.
```json 같은 코드블록 마크다운은 절대 포함하지 마.
설명 문장도 절대 포함하지 마.
                    """;

            ResponseCreateParams params = ResponseCreateParams.builder()
                    .model("gpt-4o-mini")
                    .input(prompt + "\n\nsummary:\n" + summaryJson)
                    .build();

            Response response = client.responses().create(params);

            String jsonText = response.output()
                    .get(0)
                    .message().get()
                    .content().get(0)
                    .outputText().get()
                    .text();

            return objectMapper.readValue(jsonText, AiInsightResponse.class);

        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RuntimeException("AI 리포트 생성 실패", e);
        }
    }
}