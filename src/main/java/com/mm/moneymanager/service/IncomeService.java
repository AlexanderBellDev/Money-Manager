package com.mm.moneymanager.service;

import com.mm.moneymanager.model.Income.Income;
import com.mm.moneymanager.model.Income.IncomeDTO;
import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;

import java.util.List;

public interface IncomeService {
    List<Income> getAllIncomesByUser(String username);

    Income saveIncome(IncomeDTO incomeDTO, String username);

    Income updateIncomeRecurrence(IncomeDTO incomeDTO, String username);

    boolean deleteIncome(IncomeDTO incomeDTO, String username);

    boolean deleteIncomeRecurrence(IncomeDTO incomeDTO, String username);
}
