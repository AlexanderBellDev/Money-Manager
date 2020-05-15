package com.mm.moneymanager.service.Impl;

import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.repository.DebtRepository;
import com.mm.moneymanager.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {
    private final DebtRepository debtRepository;

    @Override
    public List<Debt> getAllDebtsByUser(String username) {
        return debtRepository.findAllByUser_Username(username);
    }
}
