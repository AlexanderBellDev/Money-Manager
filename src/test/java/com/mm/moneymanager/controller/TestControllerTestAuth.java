package com.mm.moneymanager.controller;

import com.mm.moneymanager.repository.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TestAuthController.class)
@ComponentScan(basePackages = "com.mm.moneymanager.security")
class TestControllerTestAuth {
    @Autowired
    private MockMvc mvc;

    @MockBean
    UserRepository userRepository;

    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    void testAuthorizedUser() throws Exception {
        mvc.perform(get("/api/test/testcontroller")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username="admin",roles={"Visitor"})
    void testUnauthorizedUser() throws Exception {
        mvc.perform(get("/api/test/testcontroller")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void testNoUser() throws Exception {
        mvc.perform(get("/api/test/testcontroller")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

}
