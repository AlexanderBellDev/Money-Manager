package com.mm.moneymanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.DebtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DebtController.class)
@ComponentScan(basePackages = "com.mm.moneymanager.security")
class DebtControllerTest {

    @Autowired
    WebApplicationContext context;

    @MockBean
    DebtService debtService;

    @MockBean
    UserRepository userRepository;

    ObjectMapper mapper;

    User user;

    List<Debt> debtList;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("alex@alex.com")
                .firstName("alex")
                .surname("test")
                .password("password")
                .username("alex1234")
                .build();

        debtList = Collections.singletonList(new Debt(1L, "Ford", BigInteger.valueOf(10), user));

        mapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "admin")
    void getUserDebtNoData() throws Exception {
        //given

        //when
        mvc.perform(get("/api/v1/debt/userdebt")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserDebtMatchingData() throws Exception {
        //given
        given(debtService.getAllDebtsByUser("testuser")).willReturn(debtList);
        List<DebtDTO> debtDTOList = Collections.singletonList(new DebtDTO("Ford", BigInteger.valueOf(10)));

        //when
        mvc.perform(get("/api/v1/debt/userdebt")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(debtDTOList)))
                .andReturn();
    }
}