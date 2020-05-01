package com.mm.moneymanager.service;

import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.RoleName;
import com.mm.moneymanager.model.User;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    Set<Role> roleSet;

    User user;

    @BeforeEach
    void beforeEach() {
        roleSet = new HashSet<>();
        roleSet.add(new Role(1L, RoleName.ROLE_USER));

        user = new User(1L, "alex1234", "alex", "smith", "alex@alex.com", "password", roleSet);
    }

    @Test
    void testSaveUser() {
        //given
        given(userRepository.save(any(User.class))).willReturn(user);


        //when
        User returnedUser = userService.saveUser(user);


        //then
        then(userRepository).should(times(1)).save(user);
        assertNotNull(returnedUser);
        assertEquals(1L, returnedUser.getId());
        assertEquals("alex1234", returnedUser.getUsername());

    }

    @Test
    void testCheckEmailExistsNoMatch() {
        //given
        given(userRepository.findAllByEmail("alex@alex.com")).willReturn(Collections.emptyList());

        //when
        List<String> returnedEmailList = userService.checkEmailExists("alex@alex.com");

        //then
        assertTrue(returnedEmailList.isEmpty());
        then(userRepository).should(times(1)).findAllByEmail(any(String.class));

    }

    @Test
    void testCheckEmailExistsEmailMatch() {
        //given
        given(userRepository.findAllByEmail("alex@alex.com")).willReturn(Collections.singletonList(user));

        //when
        List<String> returnedEmailList = userService.checkEmailExists("alex@alex.com");

        //then
        assertFalse(returnedEmailList.isEmpty());
        assertEquals("exists",returnedEmailList.get(0));
        then(userRepository).should(times(1)).findAllByEmail(any(String.class));

    }

    @Test
    void testCheckUsernameExistsUsernameMatch() {
        //given
        given(userRepository.findAllByUsername("alex1234")).willReturn(Collections.singletonList(user));

        //when
        List<String> returnedUsernameList = userService.checkUsernameExists("alex1234");

        //then
        assertFalse(returnedUsernameList.isEmpty());
        assertEquals("exists",returnedUsernameList.get(0));
        then(userRepository).should(times(1)).findAllByUsername(any(String.class));
    }

    @Test
    void testCheckUsernameExistsNoMatch() {
        //given
        given(userRepository.findAllByUsername("alex1234")).willReturn(Collections.emptyList());

        //when
        List<String> returnedUsernameList = userService.checkUsernameExists("alex1234");

        //then
        assertTrue(returnedUsernameList.isEmpty());
        then(userRepository).should(times(1)).findAllByUsername(any(String.class));
    }
}
