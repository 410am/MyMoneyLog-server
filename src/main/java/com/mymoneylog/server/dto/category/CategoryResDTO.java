package com.mymoneylog.server.dto.category;


import com.mymoneylog.server.entity.category.Category;
import com.mymoneylog.server.enums.IncomeExpenseType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResDTO {

    private Long categoryId;

    private String categoryName;

    private IncomeExpenseType type;

    private boolean isDefault;

    private Long userId;

    public static CategoryResDTO from(Category category) {
        return CategoryResDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getName())
                .type(category.getType())
                .isDefault(category.getIsDefault())
                .userId(category.getUser().getUserId())
                .build();
    }
}
