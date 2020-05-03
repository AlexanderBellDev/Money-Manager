package com.mm.moneymanager.service.Impl;


import com.mm.moneymanager.exception.AppException;
import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.RoleName;
import com.mm.moneymanager.payload.JwtAuthenticationResponse;
import com.mm.moneymanager.repository.RoleRepository;
import com.mm.moneymanager.security.JwtTokenProvider;
import com.mm.moneymanager.service.UserService;
import com.mm.moneymanager.model.User;
import com.mm.moneymanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RoleRepository roleRepository;

    @Transactional
    public User saveUser(User user) {
       return userRepository.save(user);
    }

    public List<String> checkEmailExists(String email) {
        if (userRepository.findAllByEmail(email).isEmpty()) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList("exists");
        }
    }

    public List<String> checkUsernameExists(String username) {
        if (userRepository.findAllByUsername(username).isEmpty()) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList("exists");
        }
    }

    @Override
    public User registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));


        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<?> login(User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

}

