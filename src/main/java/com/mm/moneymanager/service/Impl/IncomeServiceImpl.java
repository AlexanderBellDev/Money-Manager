package com.mm.moneymanager.service.Impl;

import com.mm.moneymanager.model.Income.Income;
import com.mm.moneymanager.model.Income.IncomeDTO;
import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.repository.DebtRepository;
import com.mm.moneymanager.repository.IncomeRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.DebtService;
import com.mm.moneymanager.service.IncomeService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Income> getAllIncomesByUser(String username) {
        return incomeRepository.findAllByUser_UsernameAndIncomeArchived(username, false);
    }

    @Override
    public Income saveIncome(IncomeDTO incomeDTO, String username) {
        Income income = modelMapper.map(incomeDTO, Income.class);
        Optional<User> principalUser = userRepository.findByUsername(username);
        principalUser.ifPresent(income::setUser);
        return incomeRepository.save(income);
    }

    @Override
    public Income updateIncomeRecurrence(IncomeDTO incomeDTO, String username) {
        return null;
    }

    @Override
    public boolean deleteIncome(IncomeDTO incomeDTO, String username) {
        Optional<Income> optionalIncome = incomeRepository.findById(incomeDTO.getId());
        if (optionalIncome.isPresent() && optionalIncome.get().getUser().getUsername().equals(username)) {
            optionalIncome.get().setIncomeArchived(true);
            return optionalIncome.get().getIncomeArchived();
        }
        return false;
    }

    @Override
    public boolean deleteIncomeRecurrence(IncomeDTO incomeDTO, String username) {
        Optional<Income> optionalIncome = incomeRepository.findById(incomeDTO.getId());
        if (optionalIncome.isPresent() && optionalIncome.get().getUser().getUsername().equals(username)) {
            Income selectedIncome = optionalIncome.get();
            selectedIncome.setRecurringIncome(false);
            selectedIncome.setDurationOfRecurrence(null);
            incomeRepository.save(selectedIncome);
            return true;
        }
        return false;
    }
}
