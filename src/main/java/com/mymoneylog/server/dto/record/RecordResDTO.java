package com.mymoneylog.server.dto.record;

import com.mymoneylog.server.enums.IncomeExpenseType;
import com.mymoneylog.server.entity.record.Record;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordResDTO {
    private Long recordId;
    private Long userId;
    private Long categoryId;
    private IncomeExpenseType type;
    private Integer amount;
    private String memo;
    private LocalDate date;
    private LocalDateTime createdAt;
    
    public static RecordResDTO from(Record record) {

        return RecordResDTO.builder()
                .recordId(record.getRecordId())
                .userId(record.getUser().getUserId())
                .categoryId(record.getCategory().getCategoryId())
                .type(record.getType())
                .amount(record.getAmount())
                .memo(record.getMemo())
                .date(record.getDate())
                .createdAt(record.getCreatedAt())
                .build();   
    }


}
