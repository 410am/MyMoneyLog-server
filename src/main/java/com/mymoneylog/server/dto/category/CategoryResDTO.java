
package com.mymoneylog.server.dto.category;

import com.mymoneylog.server.entity.category.Category;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResDTO {

    private Long categoryId;
    private String categoryName;
    private String type;
    private boolean isDefault;

    public static CategoryResDTO from(Category category) {
        return CategoryResDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getName()) // ✅ 이름으로 변경
                .type(category.getType().name())  // enum → 문자열
                .isDefault(category.getIsDefault())
                .build();
    }
}