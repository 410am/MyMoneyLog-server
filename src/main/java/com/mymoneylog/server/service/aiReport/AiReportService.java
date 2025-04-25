package com.mymoneylog.server.service.aiReport;

import com.mymoneylog.server.dto.aiReport.AiReportReqDTO;
import com.mymoneylog.server.entity.aiReport.AiReport;
import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.repository.aiReport.AiReportRepository;
import com.mymoneylog.server.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiReportService {

    private final AiReportRepository aiReportRepository;
    private final UserRepository userRepository;

    public AiReport saveReport(AiReportReqDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        AiReport aiReport = AiReport.builder()
                .user(user)
                .content(dto.getContent())
                .month(dto.getMonth())
                .build();

        return aiReportRepository.save(aiReport);
    }
}
