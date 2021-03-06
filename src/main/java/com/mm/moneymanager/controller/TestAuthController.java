package com.mm.moneymanager.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/test")
public class TestAuthController {
    @GetMapping("/testcontroller")
    @PreAuthorize("hasRole('USER')")
    public String test(Principal currentUser){
        return "hi" + currentUser.getName();
    }
}
