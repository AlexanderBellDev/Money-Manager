package com.mm.moneymanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.model.user.UserDTO;
import com.mm.moneymanager.repository.UserRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ComponentScan(basePackages = {"com.mm.moneymanager.security", "com.mm.moneymanager.exception"})
public class UserControllerTest {

    @Autowired
    WebApplicationContext context;

    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @MockBean
    UserRepository userRepository;
    UserDTO userDTO;
    @Autowired
    private MockMvc mvc;

    String jsonContentUser;

    User user;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        userDTO = UserDTO.builder()
                .email("alex@alex.com")
                .firstName("alex")
                .surname("green")
                .username("test1234")
                .build();

        user = User.builder()
                .debts(null)
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .surname(userDTO.getSurname())
                .username(userDTO.getUsername())
                .build();


        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        jsonContentUser = mapper.writeValueAsString(userDTO);
    }

    @Test
    @WithMockUser(username = "test1234")
    void testGetUserDetails() throws Exception {
        given(userService.returnUser(userDTO.getUsername())).willReturn(userDTO);

        mvc.perform(get("/api/v1/user/userdetails")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(userDTO)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "test12345")
    void testGetUserDetailsNoContent() throws Exception {
        given(userService.returnUser(userDTO.getUsername())).willReturn(userDTO);

        mvc.perform(get("/api/v1/user/userdetails")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser("test12345")
    void testPostUserDetails() throws Exception {
        //given
        given(userService.updateUserDetails(userDTO, "test12345")).willReturn(user);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/user/userdetails")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentUser))
                //then
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("User saved"))
                .andDo(print())
                .andReturn();
    }

}
