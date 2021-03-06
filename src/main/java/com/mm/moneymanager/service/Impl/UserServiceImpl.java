package com.mm.moneymanager.service.Impl;


import com.mm.moneymanager.exception.ApiBadRequestException;
import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.RoleName;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.model.user.UserDTO;
import com.mm.moneymanager.model.user.UserLogin;
import com.mm.moneymanager.repository.RoleRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.security.JwtTokenProvider;
import com.mm.moneymanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<String> checkEmailExists(String email) {
        if (userRepository.findAllByEmail(email).isEmpty()) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList("exists");
        }
    }

    @Transactional(readOnly = true)
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
                .orElseThrow(() -> new ApiBadRequestException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));


        return userRepository.save(user);
    }

    @Override
    public String login(UserLogin loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.generateToken(authentication);

    }

    @Override
    public UserDTO returnUser(String username) {
        Optional<User> findUserByUsername = userRepository.findByUsername(username);
        return findUserByUsername.map(user -> modelMapper.map(user, UserDTO.class)).orElse(null);
    }

    @Override
    public User updateUserDetails(UserDTO userDTO, String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);

        if (byUsername.isPresent()) {
            byUsername.get().setFirstName(userDTO.getFirstName());
            byUsername.get().setSurname(userDTO.getSurname());
            byUsername.get().setUsername(userDTO.getUsername());
            return userRepository.save(byUsername.get());
        }

        return null;
    }

}

