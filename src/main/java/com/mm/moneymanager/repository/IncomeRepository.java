package com.mm.moneymanager.repository;

import com.mm.moneymanager.model.Income.Income;
import com.mm.moneymanager.model.debt.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findAllByUser_UsernameAndIncomeArchived(String username, Boolean incomeArchived);
}
