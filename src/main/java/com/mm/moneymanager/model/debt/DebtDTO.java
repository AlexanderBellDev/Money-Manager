package com.mm.moneymanager.model.debt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DebtDTO {
    @NotBlank(message = "company is mandatory")
    private String company;
    @NotNull
    private BigInteger amount;
}
