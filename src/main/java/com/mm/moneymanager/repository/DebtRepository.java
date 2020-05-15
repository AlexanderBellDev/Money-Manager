package com.mm.moneymanager.repository;

import com.mm.moneymanager.model.debt.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findAllByUser_Username(String username);
}
