package com.mm.moneymanager.service;

import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.RoleName;
import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.repository.DebtRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.Impl.DebtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DebtServiceTest {

    @InjectMocks
    DebtServiceImpl debtService;

    @Mock
    DebtRepository debtRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper modelMapper;

    Set<Role> roleSet = new HashSet<>();
    Set<Debt> debtSet = new HashSet<>();

    List<Debt> debtList;
    User user;
    User user2;


    DebtDTO debtDTO;

    Debt debt;
    Debt debtWithAlternateUsername;

    @BeforeEach
    void beforeEach() {
        roleSet.add(new Role(1L, RoleName.ROLE_USER));
        user = new User(1L, "test1234", "alex", "smith", "alex@alex.com", "password", roleSet, debtSet, null);
        user2 = new User(1L, "bob9999", "Bob", "jim", "bob@bobs.com", "password", roleSet, debtSet, null);

        debt = new Debt(1L, "Ford", BigInteger.valueOf(10), LocalDate.now(), user);
        debtWithAlternateUsername = new Debt(1L, "Ford", BigInteger.valueOf(10), LocalDate.now(), user2);
        debtSet.add(debt);
        debtList = Collections.singletonList(debt);

        debtDTO = new DebtDTO("Ford", BigInteger.valueOf(10), LocalDate.now(), 1L);

    }


    @Test
    void testGetAllDebtsByUser() {

        //given
        given(debtRepository.findAllByUser_Username(user.getUsername())).willReturn(debtList);

        //when
        List<Debt> allDebtsByUsername = debtService.getAllDebtsByUser("test1234");

        assertEquals("test1234", allDebtsByUsername.get(0).getUser().getUsername());
        then(debtRepository).should(times(1)).findAllByUser_Username(user.getUsername());
    }

    @Test
    void testSaveDebt() {
        //given
        given(modelMapper.map(debtDTO, Debt.class)).willReturn(debt);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        Debt expectedDebt = Debt.builder()
                .id(1L)
                .user(user)
                .amount(BigInteger.valueOf(10))
                .company("ford")
                .build();
        given(debtRepository.save(debt)).willReturn(expectedDebt);

        //when
        Debt actualDebt = debtService.saveDebt(debtDTO, "test1234");

        //then
        assertEquals(expectedDebt, actualDebt);

    }


    @Test
    void testDeleteDebtValidDebt() {
        given(debtRepository.findById(1L)).willReturn(Optional.ofNullable(debt));


        //when
        boolean deleteDebtResult = debtService.deleteDebt(debtDTO.getId(), "test1234");

        //then
        then(debtRepository).should(times(1)).findById(debt.getId());
        then(debtRepository).should(times(1)).delete(debt);
        assertTrue(deleteDebtResult);
    }

    @Test
    void testDeleteDebtInvalidDebt() {
        given(debtRepository.findById(1L)).willReturn(Optional.ofNullable(debtWithAlternateUsername));
        //when
        boolean deleteDebtResult = debtService.deleteDebt(debtDTO.getId(), "test1234");

        //then
        then(debtRepository).should(times(1)).findById(debtWithAlternateUsername.getId());
        then(debtRepository).should(times(0)).delete(any());
        assertFalse(deleteDebtResult);
    }

    @Test
    void testVerifyDebtDoesntExist() {
        //when
        boolean deleteDebtResult = debtService.verifyDebtExists(debtDTO.getId());

        //then
        then(debtRepository).should(times(1)).findById(debtWithAlternateUsername.getId());
        assertFalse(deleteDebtResult);
    }

    @Test
    void testVerifyDebtDoesExist() {
        //given
        given(debtRepository.findById(debtDTO.getId())).willReturn(Optional.ofNullable(debt));

        //when
        boolean deleteDebtResult = debtService.verifyDebtExists(debtDTO.getId());

        //then
        then(debtRepository).should(times(1)).findById(debtWithAlternateUsername.getId());
        assertTrue(deleteDebtResult);
    }

    @Test
    void testUpdateDebt() {
        //given
        given(debtRepository.findById(debtDTO.getId())).willReturn(Optional.ofNullable(debt));
        given(modelMapper.map(debtDTO, Debt.class)).willReturn(debt);

        boolean updateDebtResult = debtService.updateDebt(debtDTO, debtDTO.getId(), "test1234");

        //then
        then(debtRepository).should(times(1)).findById(debtDTO.getId());
        then(debtRepository).should(times(1)).save(debt);
        assertTrue(updateDebtResult);
    }
}
