package com.mm.moneymanager.controller;

import com.mm.moneymanager.model.Income.Income;
import com.mm.moneymanager.model.Income.IncomeDTO;
import com.mm.moneymanager.service.IncomeService;
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
@RequiredArgsConstructor
@Slf4j
@Secured("ROLE_USER")
@RequestMapping("/api/v1/income")
public class IncomeController {

    private final IncomeService incomeService;
    private final ModelMapper modelMapper;


    @GetMapping("/userincome")
    @Secured("ROLE_USER")
    public ResponseEntity<?> getUserIncome(Principal principal){
        List<Income> incomeList =  incomeService.getAllIncomesByUser(principal.getName());
        if (incomeList.size() == 0){
            return ResponseEntity.noContent().build();
        }
        List<IncomeDTO> incomeDTOList = Arrays.asList(modelMapper.map(incomeList, IncomeDTO[].class));
        return ResponseEntity.ok().body(incomeDTOList);
    }

    @PostMapping("/userincome")
    @Secured("ROLE_USER")
    public ResponseEntity<?> postUserIncome(Principal principal, @Valid @RequestBody IncomeDTO incomeDTO) {
        URI location = ServletUriComponentsBuilder
                .fromPath("/api/v1/income/userincome")
                .buildAndExpand(incomeService.saveIncome(incomeDTO, principal.getName())).toUri();
        return ResponseEntity.created(location).body("Income Saved");
    }

    @DeleteMapping("/userincome")
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteUserDebt(Principal principal, @Valid @RequestBody IncomeDTO incomeDTO) {
        if (!incomeService.deleteIncome(incomeDTO, principal.getName())) {
            return ResponseEntity.badRequest().body("Cannot delete income");
        }
        return ResponseEntity.ok().body("Income deleted");
    }

    @PutMapping("/userincome/{idToUpdate}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> updateUserDebt(Principal principal, @PathVariable Long idToUpdate, @Valid @RequestBody IncomeDTO incomeDTO) {
        if (!incomeService.verifyIncomeExists(idToUpdate)) {
            return ResponseEntity.notFound().build();
        }

        if (!incomeService.updateIncome(incomeDTO, idToUpdate, principal.getName())) {
            return ResponseEntity.badRequest().body("Cannot update income");
        }
        return ResponseEntity.ok().body("Income updated");
    }


}
