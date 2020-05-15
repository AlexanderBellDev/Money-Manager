package com.mm.moneymanager.controller;

import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;
import com.mm.moneymanager.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/debt")
@RequiredArgsConstructor
public class DebtController {
    private final DebtService debtService;
    private final ModelMapper modelMapper;

    @GetMapping("/userdebt")
    @Secured("ROLE_USER")
    public ResponseEntity<?> getUserDebt(Principal principal) {
        List<Debt> allDebtsByUser = debtService.getAllDebtsByUser(principal.getName());
        if (allDebtsByUser.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        List<DebtDTO> debtDTOList = Arrays.asList(modelMapper.map(allDebtsByUser, DebtDTO[].class));
        return ResponseEntity.ok(debtDTOList);
    }
}
