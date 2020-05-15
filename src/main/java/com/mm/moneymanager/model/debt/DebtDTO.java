package com.mm.moneymanager.model.debt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtDTO {
    private String company;
    private BigInteger amount;
}
