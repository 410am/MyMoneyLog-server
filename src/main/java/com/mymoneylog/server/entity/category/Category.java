package com.mymoneylog.server.entity.category;


import com.mymoneylog.server.entity.category.Category;

import java.time.LocalDateTime;

import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.enums.IncomeExpenseType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private IncomeExpenseType type;

    @Builder.Default
    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    
} }


//     @Enumerated(EnumType.STRING)
//     @Column(name = "type", nullable = false)
//     private CategoryType type;





