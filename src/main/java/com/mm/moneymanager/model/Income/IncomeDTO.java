package com.mm.moneymanager.model.Income;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class IncomeDTO {
    @NotNull
    BigDecimal incomeAmount;
    @NotBlank(message = "income source is mandatory")
    String incomeSource;
    @NotNull
    Boolean recurringIncome;

    @NotNull
    private LocalDate paymentDate;

    Integer durationOfRecurrence;

    private Long id;
}
