package com.mymoneylog.server.dto.aiReport;


import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiInsightResponse {
    public String headline;
    public List<String> tips;
}