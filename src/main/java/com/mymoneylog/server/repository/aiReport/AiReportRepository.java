package com.mymoneylog.server.repository.aiReport;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mymoneylog.server.entity.aiReport.AiReport;

public interface AiReportRepository extends JpaRepository<AiReport, Long> {
}
