package com.mymoneylog.server.dto.dashboard;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDto {

    private Long totalIncome;       // 총 수입 
    private Long totalExpense;      // 총 지출 
    private Long incomeCount;        // 수입 건수 
    private Long expenseCount;       // 지출 건수 

    

    public Long getBalance() {       
        return totalIncome - totalExpense;
    }
}
