package com.mm.moneymanager.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.model.user.UserLogin;
import com.mm.moneymanager.repository.RoleRepository;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@ComponentScan(basePackages = "com.mm.moneymanager.security")
class AuthControllerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    WebApplicationContext context;

    @MockBean
    UserService userService;

    @MockBean
    RoleRepository roleRepository;

    ObjectMapper mapper;

    User user;

    UserLogin loginUser;

    String jsonContentLoginUser;
    String jsonContentUser;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        user = User.builder()
                .email("alex@alex.com")
                .firstName("alex")
                .surname("test")
                .password("password")
                .username("test1234")
                .build();

        mapper = new ObjectMapper();

        loginUser = UserLogin.builder()
                .username("alex")
                .build();

        jsonContentUser = mapper.writeValueAsString(user);
    }

    @Test
    void testCheckEmailExistsMatch() throws Exception {
        //given
        given(userService.checkEmailExists("alex@alex.com")).willReturn(Collections.singletonList("exists"));

        //when
        mvc.perform(get("/api/v1/auth/checkemail/alex@alex.com")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[\"exists\"]"))
                .andReturn();
    }

    @Test
    void testCheckEmailExistsNoMatch() throws Exception {
        //given
        given(userService.checkEmailExists("alex@alex.com")).willReturn(Collections.emptyList());

        //when
        mvc.perform(get("/api/v1/auth/checkemail/alex@alex.com")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn();
    }

    @Test
    void testCheckUsernameExistsNoMatch() throws Exception {
        //given
        given(userService.checkUsernameExists("alex")).willReturn(Collections.emptyList());

        //when
        mvc.perform(get("/api/v1/auth/checkusername/alex")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn();
    }

    @Test
    void testCheckUsernameExistsMatch() throws Exception {
        //given
        given(userService.checkUsernameExists("alex")).willReturn(Collections.singletonList("exists"));

        //when
        mvc.perform(get("/api/v1/auth/checkusername/alex")
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[\"exists\"]"))
                .andReturn();
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        //given
        given(userService.checkUsernameExists(user.getUsername())).willReturn(Collections.emptyList());
        given(userService.checkEmailExists(user.getEmail())).willReturn(Collections.emptyList());

        //when
        mvc.perform(post("/api/v1/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentUser))
                //then
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void testRegisterUserUsernameExists() throws Exception {
        //given
        given(userService.checkUsernameExists(user.getUsername())).willReturn(Collections.singletonList("exists"));
        given(userService.checkEmailExists(user.getEmail())).willReturn(Collections.emptyList());

        //when
        mvc.perform(post("/api/v1/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentUser))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Username is already taken\",\"httpStatus\":\"BAD_REQUEST\"}"))
                .andReturn();

    }

    @Test
    void testRegisterUserEmailExists() throws Exception {
        //given
        given(userService.checkUsernameExists(user.getUsername())).willReturn(Collections.emptyList());
        given(userService.checkEmailExists(user.getEmail())).willReturn(Collections.singletonList("exists"));

        //when
        mvc.perform(post("/api/v1/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentUser))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Email is already taken\",\"httpStatus\":\"BAD_REQUEST\"}"))
                .andReturn();

    }

    @Test
    void testAuthenticateUserGoodCredentials() throws Exception {
        //given
        loginUser.setPassword("password");
        jsonContentLoginUser = mapper.writeValueAsString(loginUser);
        doReturn("12345").when(userService).login(loginUser);

        //when
        mvc.perform(post("/api/v1/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentLoginUser))
                //then
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testAuthenticateUserInvalidCredentials() throws Exception {
        jsonContentLoginUser = mapper.writeValueAsString(loginUser);
        //when
        mvc.perform(post("/api/v1/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentLoginUser))
                //then
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void testAuthenticateUserBadCredentials() throws Exception {
        //given
        loginUser.setPassword("password");
        jsonContentLoginUser = mapper.writeValueAsString(loginUser);


        //when
        doThrow(BadCredentialsException.class).when(userService).login(loginUser);
        mvc.perform(post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContentLoginUser))
                //then
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
