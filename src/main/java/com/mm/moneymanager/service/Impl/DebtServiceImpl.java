package com.mm.moneymanager.service.Impl;

import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.repository.DebtRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Debt> getAllDebtsByUser(String username) {
        return debtRepository.findAllByUser_Username(username);
    }

    @Override
    public Debt saveDebt(DebtDTO debtDTO, String username) {
        Debt debt = modelMapper.map(debtDTO, Debt.class);

        Optional<User> principalUser = userRepository.findByUsername(username);
        principalUser.ifPresent(debt::setUser);

        return debtRepository.save(debt);
    }

    @Override
    public List<Debt> deleteDebt(List<DebtDTO> debtDTO, String username) {
        List<Debt> debtsToDelete = new ArrayList<>();
        for (DebtDTO dto : debtDTO) {
            Optional<Debt> findDebtById = debtRepository.findById(dto.getId());
            if (findDebtById.isPresent()) {
                if (findDebtById.get().getUser().getUsername().equals(username)) {
                    debtsToDelete.add(findDebtById.get());
                }
            }
        }
        debtRepository.deleteAll(debtsToDelete);

        return debtsToDelete;
    }

}
