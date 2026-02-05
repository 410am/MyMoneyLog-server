package com.mymoneylog.server.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.mymoneylog.server.entity.category.Category;
import com.mymoneylog.server.entity.record.Record;
import com.mymoneylog.server.dto.user.req.UserReqDTO;
import com.mymoneylog.server.entity.aiReport.AiReport;

@Entity
@Table(name = "TB_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String picture;

    private String email;

    private String password;

    private String nickname;

    // private String provider;     // ex) google, kakao
    private String providerId;   // ex) 구글의 sub

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    // 생성일자 자동 설정
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    
    // 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiReport> aiReports = new ArrayList<>();


    public String getProvider() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProvider'");
    }


      // 상태 변경 메서드
      public void update(String nickname) {
        // this.email = dto.getEmail();
        this.nickname = nickname;
        // this.picture = dto.getPicture();
        // this.password = dto.getPassword();
    }


      public void setSub(String sub) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSub'");
      }


      


}
