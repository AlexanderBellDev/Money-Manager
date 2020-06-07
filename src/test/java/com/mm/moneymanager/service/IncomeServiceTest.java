package com.mm.moneymanager.service;

import com.mm.moneymanager.model.Income.Income;
import com.mm.moneymanager.model.Income.IncomeDTO;
import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.RoleName;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.repository.IncomeRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.Impl.IncomeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class IncomeServiceTest {

    @InjectMocks
    IncomeServiceImpl incomeService;

    @Mock
    IncomeRepository incomeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper modelMapper;

    Set<Role> roleSet = new HashSet<>();
    Set<Income> incomeSet = new HashSet<>();

    List<Income> incomes;
    User user;
    User user2;

    IncomeDTO incomeDTO;

    Income income;
    Income incomeWithAlternativeUser;

    @BeforeEach
    void beforeEach() {
        roleSet.add(new Role(1L, RoleName.ROLE_USER));
        user = new User(1L, "test123", "alex", "smith", "alex@alex.com", "password", roleSet, null, incomeSet);
        user2 = new User(1L, "test245", "Bob", "jim", "bob@bobs.com", "password", roleSet, null, incomeSet);

        income = new Income(1L, 1L, BigDecimal.valueOf(1000.00), "Ford", true, false, 3, LocalDate.now(), user);
        incomeWithAlternativeUser = new Income(1L, 1L, BigDecimal.valueOf(1000.00), "Ford", true, false, 3, LocalDate.now(), user2);
        incomeSet.add(income);
        incomes = Collections.singletonList(income);

        incomeDTO = new IncomeDTO(BigDecimal.valueOf(1000.00), "Ford", true, LocalDate.now(), 3, 1L);

    }


    @Test
    void getAllIncomeByUser() {

        //given
        given(incomeRepository.findAllByUser_UsernameAndIncomeArchived(user.getUsername(), false)).willReturn(incomes);

        //when
        List<Income> allIncomeByUsername = incomeService.getAllIncomesByUser("test123");

        assertEquals("test123", allIncomeByUsername.get(0).getUser().getUsername());
        then(incomeRepository).should(times(1)).findAllByUser_UsernameAndIncomeArchived(user.getUsername(), false);
    }

    @Test
    void saveIncome() {
        //given
        given(modelMapper.map(incomeDTO, Income.class)).willReturn(income);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        Income expectedIncome = Income.builder()
                .id(1L)
                .user(user)
                .incomeAmount(BigDecimal.valueOf(1000.00))
                .incomeSource("ford")
                .recurringIncome(true)
                .durationOfRecurrence(3)
                .incomeArchived(false)
                .build();
        given(incomeRepository.save(income)).willReturn(expectedIncome);

        //when
        Income actualIncome = incomeService.saveIncome(incomeDTO, "test123");

        //then
        assertEquals(expectedIncome, actualIncome);
    }


    @Test
    void deleteIncomeValidIncome() {
        given(incomeRepository.findById(1L)).willReturn(Optional.ofNullable(income));

        //when
        boolean deleteIncomeResult = incomeService.deleteIncome(incomeDTO, "test123");

        //then
        assertTrue(deleteIncomeResult);
    }

    @Test
    void deleteIncomeInvalidIncome() {
        given(incomeRepository.findById(1L)).willReturn(Optional.ofNullable(incomeWithAlternativeUser));
        //when
        boolean deleteIncomeResult = incomeService.deleteIncome(incomeDTO, "test123");

        //then
        assertFalse(deleteIncomeResult);
    }

    @Test
    void deleteIncomeDoesntExist() {
        //when
        boolean deleteIncomeResult = incomeService.deleteIncome(incomeDTO, "test123");

        //then
        assertFalse(deleteIncomeResult);
    }

    @Test
    void testVerifyIncomeDoesntExist() {
        //when
        boolean deleteIncomeResult = incomeService.verifyIncomeExists(incomeDTO.getId());

        //then
        then(incomeRepository).should(times(1)).findById(incomeWithAlternativeUser.getId());
        assertFalse(deleteIncomeResult);
    }

    @Test
    void testVerifyIncomeDoesExist() {
        //given
        given(incomeRepository.findById(incomeDTO.getId())).willReturn(Optional.ofNullable(income));

        //when
        boolean deleteIncomeResult = incomeService.verifyIncomeExists(incomeDTO.getId());

        //then
        then(incomeRepository).should(times(1)).findById(incomeWithAlternativeUser.getId());
        assertTrue(deleteIncomeResult);
    }

    @Test
    void testUpdateIncome() {
        //given
        given(incomeRepository.findById(incomeDTO.getId())).willReturn(Optional.ofNullable(income));
        given(modelMapper.map(incomeDTO, Income.class)).willReturn(income);

        boolean updateIncomeResult = incomeService.updateIncome(incomeDTO, incomeDTO.getId(), "test123");

        //then
        then(incomeRepository).should(times(1)).findById(incomeDTO.getId());
        then(incomeRepository).should(times(1)).save(income);
        assertTrue(updateIncomeResult);
    }

}
