package com.mm.moneymanager.service;

import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;

import java.util.List;

public interface DebtService {
    List<Debt> getAllDebtsByUser(String username);

    Debt saveDebt(DebtDTO debtDTO, String username);

    List<Debt> deleteDebt(List<DebtDTO> debtDTO, String username);
}
