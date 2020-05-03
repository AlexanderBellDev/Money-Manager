package com.mm.moneymanager.controller;

import com.mm.moneymanager.exception.AppException;
import com.mm.moneymanager.payload.ApiResponse;
import com.mm.moneymanager.payload.JwtAuthenticationResponse;
import com.mm.moneymanager.security.CurrentUser;
import com.mm.moneymanager.security.UserPrincipal;
import com.mm.moneymanager.service.UserService;
import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.RoleName;
import com.mm.moneymanager.model.User;
import com.mm.moneymanager.repository.RoleRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
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

        if(!userService.checkEmailExists(user.getEmail()).isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email is already taken"));
        }

        if(!userService.checkUsernameExists(user.getUsername()).isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Username is already taken"));
        }

        userService.registerUser(user);

        URI location = ServletUriComponentsBuilder
                .fromPath("/api/users/{username}")
                .buildAndExpand(user.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody User loginRequest) {
        return userService.login(loginRequest);
    }




//    @GetMapping("/{pollId}")
//    public PollResponse getPollById(@CurrentUser UserPrincipal currentUser,
//                                    @PathVariable Long pollId) {
//        return pollService.getPollById(pollId, currentUser);
//    }

}
