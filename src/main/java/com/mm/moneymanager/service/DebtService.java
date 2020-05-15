package com.mm.moneymanager.service;

import com.mm.moneymanager.model.debt.Debt;

import java.util.List;

public interface DebtService {
    List<Debt> getAllDebtsByUser(String username);
}
