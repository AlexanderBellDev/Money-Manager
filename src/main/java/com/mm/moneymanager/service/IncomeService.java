package com.mm.moneymanager.service;

import com.mm.moneymanager.model.Income.Income;
import com.mm.moneymanager.model.Income.IncomeDTO;

import java.util.List;

public interface IncomeService {
    List<Income> getAllIncomesByUser(String username);

    Income saveIncome(IncomeDTO incomeDTO, String username);

    boolean deleteIncome(IncomeDTO incomeDTO, String username);

    boolean verifyIncomeExists(Long id);

    boolean updateIncome(IncomeDTO incomeDTO, Long id, String username);
}
