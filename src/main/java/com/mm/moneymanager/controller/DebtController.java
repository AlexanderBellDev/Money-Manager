package com.mm.moneymanager.controller;

import com.mm.moneymanager.exception.ApiBadRequestException;
import com.mm.moneymanager.exception.ApiNoContentException;
import com.mm.moneymanager.exception.ApiNotFoundException;
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
@Secured("ROLE_USER")
public class DebtController {
    private final DebtService debtService;
    private final ModelMapper modelMapper;

    @GetMapping("/userdebt")
    public ResponseEntity<?> getUserDebt(Principal principal) {
        List<Debt> allDebtsByUser = debtService.getAllDebtsByUser(principal.getName());
        if (allDebtsByUser.size() == 0) {
            throw new ApiNoContentException("No Debt/s Found");
        }

        List<DebtDTO> debtDTOList = Arrays.asList(modelMapper.map(allDebtsByUser, DebtDTO[].class));
        return ResponseEntity.ok(debtDTOList);
    }

    @PostMapping("/userdebt")
    public ResponseEntity<?> postUserDebt(Principal principal, @Valid @RequestBody DebtDTO debtDTO) {
        URI location = ServletUriComponentsBuilder
                .fromPath("/api/v1/debt/userdebt")
                .buildAndExpand(debtService.saveDebt(debtDTO, principal.getName())).toUri();
        return ResponseEntity.created(location).body("Debt saved");
    }


    @DeleteMapping("/userdebt/{idToDelete}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteUserDebt(Principal principal, @PathVariable Long idToDelete) {
        if (!debtService.verifyDebtExists(idToDelete)) {
            throw new ApiNotFoundException("Debt not found");
        }
        if (!debtService.deleteDebt(idToDelete, principal.getName())) {
            throw new ApiBadRequestException("Cannot delete debt");
        }
        return ResponseEntity.ok().body("Debt deleted");
    }

    @PutMapping("/userdebt/{idToUpdate}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> updateUserDebt(Principal principal, @PathVariable Long idToUpdate, @Valid @RequestBody DebtDTO debtDTO) {
        if (!debtService.verifyDebtExists(idToUpdate)) {
            throw new ApiNotFoundException("Debt not found");
        }

        if (!debtService.updateDebt(debtDTO, idToUpdate, principal.getName())) {
            throw new ApiBadRequestException("Cannot update debt");
        }
        return ResponseEntity.ok().body("Debt updated");
    }

}
