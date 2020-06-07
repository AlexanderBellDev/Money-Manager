package com.mm.moneymanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mm.moneymanager.model.Income.Income;
import com.mm.moneymanager.model.Income.IncomeDTO;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.repository.RoleRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.IncomeService;
import com.mm.moneymanager.service.UserService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IncomeController.class)
@ComponentScan(basePackages = {"com.mm.moneymanager.security", "com.mm.moneymanager.exception"})
public class IncomeControllerTest {

    @Autowired
    WebApplicationContext context;

    @MockBean
    IncomeService incomeService;

    @MockBean
    RoleRepository repository;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserRepository userRepository;

    User user;

    List<Income> incomeList;
    List<IncomeDTO> incomeDTOList;

    IncomeDTO incomeDTO;

    ObjectMapper mapper;

    String jsonContentIncome;
    String jsonContentIncomeMalformed;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        user = User.builder()
                .email("alex@alex.com")
                .firstName("alex")
                .surname("test")
                .password("password")
                .username("alex1234")
                .build();

        Income testIncome = new Income(1L, 1L, BigDecimal.valueOf(1000.00), "Salary", true, false, 12, LocalDate.now(), user);
        incomeList = Collections.singletonList(testIncome);

        incomeDTO = new IncomeDTO(BigDecimal.valueOf(1000.00), "Salary", true, LocalDate.now(), 12, 1L);
        incomeDTOList = Collections.singletonList(incomeDTO);

        IncomeDTO incomeTestMalformed = IncomeDTO.builder()
                .incomeAmount(BigDecimal.valueOf(10))
                .build();


        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        jsonContentIncome = mapper.writeValueAsString(incomeDTO);

        jsonContentIncomeMalformed = mapper.writeValueAsString(incomeTestMalformed);
    }

    @Test
    @WithMockUser(username = "admin")
    void getUserIncome() throws Exception {
        //given
        given(incomeService.getAllIncomesByUser("admin")).willReturn(incomeList);

        //when
        MvcResult mvcResult = mvc.perform(get("/api/v1/income/userincome")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(incomeDTOList)))
                //then
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse());
    }


    @Test
    @WithMockUser(username = "testuser")
    void testDeleteIncome() throws Exception {
        //given
        given(incomeService.deleteIncome(incomeDTO, "testuser")).willReturn(true);
        //when
        MvcResult income_deleted = mvc.perform(MockMvcRequestBuilders.delete("/api/v1/income/userincome")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentIncome)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Income deleted"))
                .andDo(print())
                .andReturn();

        assertNotNull(income_deleted.getResponse());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteIncomeUnsuccessful() throws Exception {
        //given
        given(incomeService.deleteIncome(incomeDTO, "testuser")).willReturn(false);

        //when
        MvcResult cannot_delete_income = mvc.perform(MockMvcRequestBuilders.delete("/api/v1/income/userincome")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentIncome)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Cannot delete income"))
                .andDo(print())
                .andReturn();

        assertNotNull(cannot_delete_income.getResponse());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testSaveIncome() throws Exception {
        //given

        //when
        MvcResult income_saved = mvc.perform(MockMvcRequestBuilders.post("/api/v1/income/userincome")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentIncome))
                //then
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Income Saved"))
                .andDo(print())
                .andReturn();

        assertNotNull(income_saved.getResponse());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testSaveMalformedIncome() throws Exception {
        //given

        //when
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/v1/income/userincome")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentIncomeMalformed))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"recurringIncome\":\"must not be null\",\"incomeSource\":\"income source is mandatory\",\"paymentDate\":\"must not be null\"}"))
                .andDo(print())
                .andReturn();

        assertNotNull(mvcResult.getResponse());
    }


    @Test
    @WithMockUser(username = "testuser")
    void testUpdateIncome() throws Exception {
        //given
        given(incomeService.verifyIncomeExists(incomeDTO.getId())).willReturn(true);
        given(incomeService.updateIncome(incomeDTO, incomeDTO.getId(), "testuser")).willReturn(true);

        //when
        MvcResult income_updated = mvc.perform(MockMvcRequestBuilders.put("/api/v1/income/userincome/" + incomeDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonContentIncome)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Income updated"))
                .andDo(print())
                .andReturn();

        assertNotNull(income_updated.getResponse());
    }


}
