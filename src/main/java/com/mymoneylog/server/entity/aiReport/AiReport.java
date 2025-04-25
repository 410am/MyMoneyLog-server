package com.mymoneylog.server.entity.aiReport;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.mymoneylog.server.entity.user.User;

@Entity
@Table(name = "TB_AI_REPORTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_report_id")
    private Long aiReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래키
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 7)
    private String month;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 생성일자 자동 설정
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
