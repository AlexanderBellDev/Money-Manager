package com.mm.moneymanager.model.Income;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

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

    Integer durationOfRecurrence;

    private Long id;
}
