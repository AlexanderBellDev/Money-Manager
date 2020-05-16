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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


    DebtDTO debtDTO;

    Debt debt;

    @BeforeEach
    void beforeEach() {
        roleSet.add(new Role(1L, RoleName.ROLE_USER));
        user = new User(1L, "alex1234", "alex", "smith", "alex@alex.com", "password", roleSet, debtSet);

        debt = new Debt(1L, "Ford", BigInteger.valueOf(10), user);
        debtSet.add(debt);
        debtList = Collections.singletonList(debt);

        debtDTO = new DebtDTO("Ford", BigInteger.valueOf(10));

    }


    @Test
    void getAllDebtsByUser() {

        //given
        given(debtRepository.findAllByUser_Username(user.getUsername())).willReturn(debtList);

        //when
        List<Debt> allDebtsByUsername = debtService.getAllDebtsByUser("alex1234");

        assertEquals("alex1234", allDebtsByUsername.get(0).getUser().getUsername());
        then(debtRepository).should(times(1)).findAllByUser_Username(user.getUsername());
    }

    @Test
    void saveDebt() {
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
        Debt actualDebt = debtService.saveDebt(debtDTO, "alex1234");

        //then
        assertEquals(expectedDebt, actualDebt);

    }
}