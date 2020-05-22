package com.mm.moneymanager.service;

import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;

import java.util.List;

public interface DebtService {
    List<Debt> getAllDebtsByUser(String username);

    Debt saveDebt(DebtDTO debtDTO, String username);

    boolean deleteDebt(Long debtToDeleteID, String username);

    boolean verifyDebtExists(Long id);

    boolean updateDebt(DebtDTO debtDTO, Long id, String username);
}
