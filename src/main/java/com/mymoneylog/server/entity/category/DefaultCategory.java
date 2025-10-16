package com.mymoneylog.server.entity.category;

import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.enums.IncomeExpenseType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_DEFAULT_CATEGORY")
@Getter @NoArgsConstructor
@Setter
@AllArgsConstructor
@Builder
public class DefaultCategory {

    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long defaultCategoryId; 

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private IncomeExpenseType type;

    @Builder.Default
    @Column(name = "is_default")
    private Boolean isDefault = true;


    public DefaultCategory(User user, String name, IncomeExpenseType type) {
        this.name = name;
        this.type = type;
        this.isDefault = true;
    }
}

