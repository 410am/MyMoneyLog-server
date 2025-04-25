package com.mymoneylog.server.dto.category;

import com.mymoneylog.server.enums.IncomeExpenseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryReqDTO {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;

    @NotNull(message = "카테고리 타입은 필수입니다.")
    private IncomeExpenseType type;

    private boolean isDefault;
}
