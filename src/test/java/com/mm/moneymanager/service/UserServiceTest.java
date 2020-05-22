package com.mm.moneymanager.service;

import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.RoleName;
import com.mm.moneymanager.model.debt.Debt;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.model.user.UserLogin;
import com.mm.moneymanager.repository.RoleRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.security.JwtTokenProvider;
import com.mm.moneymanager.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    RoleRepository roleRepository;

    @Mock
    Authentication authentication;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    Set<Role> roleSet = new HashSet<>();
    Set<Debt> debtSet = new HashSet<>();

    User user;

    @BeforeEach
    void beforeEach() {
        roleSet.add(new Role(1L, RoleName.ROLE_USER));
        debtSet.add(new Debt());

        user = new User(1L, "alex1234", "alex", "smith", "alex@alex.com", "password", roleSet, debtSet, null);
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
        assertEquals("exists", returnedEmailList.get(0));
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
        assertEquals("exists", returnedUsernameList.get(0));
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

    @Test
    void registerUserTest() {
        User userNew = User.builder()
                .email("alex@alex.com")
                .firstName("alex")
                .surname("test")
                .password("password")
                .username("test1234")
                .build();

        given(passwordEncoder.encode(userNew.getPassword())).willReturn("ABCD");

        Role userRole = new Role(2L, RoleName.ROLE_USER);

        given(roleRepository.findByName(RoleName.ROLE_USER)).willReturn(Optional.of(userRole));

        given(userRepository.save(userNew)).willReturn(user);

        User userResult = userService.registerUser(userNew);

        assertNotNull(userResult);

        assertEquals("alex@alex.com", userResult.getEmail());

        then(userRepository).should(times(1)).save(userNew);

    }

    @Test
    void loginTest() {
            UserLogin loginRequest = UserLogin.builder()
                .username("alex")
                .password("password")
                .build();

        //given
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        given(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).willReturn(authentication);


        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(usernamePasswordAuthenticationToken));

        given(jwtTokenProvider.generateToken(authentication)).willReturn("12345");


        String returnedToken = userService.login(loginRequest);


      assertEquals("12345",returnedToken);

        then(authenticationManager).should(times(2)).authenticate(any());
        then(jwtTokenProvider).should(times(1)).generateToken(authentication);
    }


}
