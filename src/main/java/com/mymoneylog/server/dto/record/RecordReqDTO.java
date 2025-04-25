package com.mymoneylog.server.dto.record;

import com.mymoneylog.server.enums.IncomeExpenseType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordReqDTO {
    private Long userId;
    private Long categoryId;
    private IncomeExpenseType type;
    private Integer amount;
    private String memo;
    private LocalDate date;
}
