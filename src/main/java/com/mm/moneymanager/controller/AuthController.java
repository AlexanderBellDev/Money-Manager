package com.mm.moneymanager.controller;

import com.mm.moneymanager.exception.ApiBadRequestException;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.model.user.UserLogin;
import com.mm.moneymanager.payload.ApiResponse;
import com.mm.moneymanager.payload.JwtAuthenticationResponse;
import com.mm.moneymanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://localhost:4200", "https://trelloclone.cfapps.io"}, maxAge = 3600, allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/checkemail/{email}")
    public ResponseEntity<?> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.checkEmailExists(email));
    }

    @GetMapping("/checkusername/{username}")
    public ResponseEntity<?> checkUsernameExists(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.checkUsernameExists(username));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {

        if (!userService.checkEmailExists(user.getEmail()).isEmpty()) {
            throw new ApiBadRequestException("Email is already taken");
        }

        if (!userService.checkUsernameExists(user.getUsername()).isEmpty()) {
            throw new ApiBadRequestException("Username is already taken");
        }

        userService.registerUser(user);

        URI location = ServletUriComponentsBuilder
                .fromPath("/api/v1/user/userdetails/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLogin loginRequest) {
        return ResponseEntity.ok(new JwtAuthenticationResponse(userService.login(loginRequest)));
    }
}
