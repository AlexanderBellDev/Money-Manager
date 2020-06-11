package com.mm.moneymanager.controller;

import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.model.user.UserDTO;
import com.mm.moneymanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/userdetails")
    public ResponseEntity<?> getUserDetails(Principal principal) {
        UserDTO userDTO = null;
        if (principal.getName() != null) {
            userDTO = userService.returnUser(principal.getName());
        }
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/userdetails")
    public ResponseEntity<?> saveUserDetails(Principal principal, @RequestBody UserDTO userDTO) {
        User user = userService.updateUserDetails(userDTO, principal.getName());
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok("User saved");
    }
}
