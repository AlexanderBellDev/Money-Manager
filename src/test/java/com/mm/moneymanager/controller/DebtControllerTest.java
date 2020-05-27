package com.mm.moneymanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.debt.DebtDTO;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.DebtService;
import org.hamcrest.core.Is;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DebtController.class)
@ComponentScan(basePackages = {"com.mm.moneymanager.security", "com.mm.moneymanager.exception"})
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

    String jsonContentDebt;
    String jsonContentDebtMalformed;

    DebtDTO fordDebtDTO;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        user = User.builder()
                .email("alex@alex.com")
                .firstName("alex")
                .surname("test")
                .password("password")
                .username("test1234")
                .build();

        Debt fordDebt = new Debt(1L, "Ford", BigInteger.valueOf(10), LocalDate.now(), user);
        debtList = Collections.singletonList(fordDebt);

        fordDebtDTO = new DebtDTO("ford", BigInteger.valueOf(10), LocalDate.now(), 1L);

        DebtDTO fordDebtDTOMalformed = DebtDTO.builder()
                .amount(BigInteger.valueOf(10))
                .build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        jsonContentDebt = mapper.writeValueAsString(fordDebtDTO);

        jsonContentDebtMalformed = mapper.writeValueAsString(fordDebtDTOMalformed);
    }

    @Test
    @WithMockUser(username = "admin")
    void testGetUserDebtNoData() throws Exception {
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
    void testGetUserDebtMatchingData() throws Exception {
        //given
        given(debtService.getAllDebtsByUser("testuser")).willReturn(debtList);
        List<DebtDTO> debtDTOList = Collections.singletonList(new DebtDTO("Ford", BigInteger.valueOf(10), LocalDate.now(), 1L));

        //when
        mvc.perform(get("/api/v1/debt/userdebt")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(debtDTOList)))
                .andReturn();
    }


    @Test
    @WithMockUser(username = "testuser")
    void testSaveUserDebt() throws Exception {
        //given

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/debt/userdebt")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentDebt))
                //then
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Debt saved"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testSaveUserDebtMalformedObject() throws Exception {
        //given

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/debt/userdebt")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentDebtMalformed)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.company", Is.is("company is mandatory")))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteDebt() throws Exception {
        //given
        given(debtService.verifyDebtExists(fordDebtDTO.getId())).willReturn(true);
        given(debtService.deleteDebt(fordDebtDTO.getId(), "testuser")).willReturn(true);

        //when
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/debt/userdebt/" + fordDebtDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentDebt)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Debt deleted"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteDebtUnsuccessful() throws Exception {
        //given
        given(debtService.verifyDebtExists(fordDebtDTO.getId())).willReturn(true);
        given(debtService.deleteDebt(fordDebtDTO.getId(), "testuser")).willReturn(false);

        //when
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/debt/userdebt/" + fordDebtDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentDebt)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Cannot delete debt"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteDebtDoesntExist() throws Exception {
        //given
        given(debtService.verifyDebtExists(fordDebtDTO.getId())).willReturn(false);

        //when
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/debt/userdebt/" + fordDebtDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentDebt)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testUpdateDebt() throws Exception {
        //given
        given(debtService.verifyDebtExists(fordDebtDTO.getId())).willReturn(true);
        given(debtService.updateDebt(fordDebtDTO, fordDebtDTO.getId(), "testuser")).willReturn(true);

        //when
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/debt/userdebt/" + fordDebtDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentDebt)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                //    .andExpect(MockMvcResultMatchers.content().string("Debt deleted"))
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testUpdateDebtDoesntExist() throws Exception {
        //given
        given(debtService.verifyDebtExists(fordDebtDTO.getId())).willReturn(false);

        //when
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/debt/userdebt/" + fordDebtDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentDebt)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testuser")
    void testUpdateDebtUnsuccessful() throws Exception {
        //given
        given(debtService.verifyDebtExists(fordDebtDTO.getId())).willReturn(true);
        given(debtService.updateDebt(fordDebtDTO, fordDebtDTO.getId(), "testuser")).willReturn(false);

        //when
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/debt/userdebt/" + fordDebtDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentDebt)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Cannot update debt"))
                .andDo(print())
                .andReturn();
    }

}