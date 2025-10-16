// package com.mymoneylog.server.dto.category;


// import com.fasterxml.jackson.annotation.JsonPropertyOrder;
// import com.mymoneylog.server.entity.category.Category;
// import com.mymoneylog.server.enums.IncomeExpenseType;
// import lombok.*;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class CategoryResDTO {

//     private Long categoryId;

//     private String categoryName;

//     private IncomeExpenseType type;

//     private boolean isDefault;

//     private Long userId;

//     public static CategoryResDTO from(Category category) {
//         if (category == null) return null;

//         return CategoryResDTO.builder()
//                 .categoryId(category.getCategoryId())
//                 .categoryName(category.getName())
//                 .type(category.getType())
//                 .isDefault(category.getIsDefault())
//                 .build();
//     }
// }

package com.mymoneylog.server.dto.category;

import com.mymoneylog.server.entity.category.Category;
import com.mymoneylog.server.enums.IncomeExpenseType;
import lombok.*;

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class CategoryResDTO {
//     private Long categoryId;
//     private String name;
//     private IncomeExpenseType type;
//     private Boolean isDefault;

//     // ✅ Category 엔티티 → DTO 변환 메서드
//     public static CategoryResDTO from(Category category) {
//         if (category == null) return null;

//         return CategoryResDTO.builder()
//                 .categoryId(category.getCategoryId())
//                 .name(category.getName())
//                 .type(category.getType())
//                 .isDefault(category.getIsDefault())
//                 .build();
//     }
// }


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