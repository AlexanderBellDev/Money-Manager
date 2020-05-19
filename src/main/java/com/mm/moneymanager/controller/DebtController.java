package com.mm.moneymanager.controller;

import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;
import com.mm.moneymanager.service.DebtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/debt")
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping("/userdebt")
    @Secured("ROLE_USER")
    public ResponseEntity<?> postUserDebt(Principal principal, @Valid @RequestBody DebtDTO debtDTO) {
        URI location = ServletUriComponentsBuilder
                .fromPath("/api/v1/debt/userdebt")
                .buildAndExpand(debtService.saveDebt(debtDTO, principal.getName())).toUri();
        return ResponseEntity.created(location).body("Debt saved");
    }

    @DeleteMapping("/userdebt")
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteUserDebt(Principal principal, @Valid @RequestBody DebtDTO debtDTO) {
        if (!debtService.deleteDebt(debtDTO, principal.getName())) {
            return ResponseEntity.badRequest().body("Cannot delete debt");
        }
        return ResponseEntity.ok().body("Debt deleted");
    }

}
