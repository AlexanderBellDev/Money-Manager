package com.mm.moneymanager.service;


import com.mm.moneymanager.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<String> checkEmailExists(String email);
    List<String> checkUsernameExists(String username);

    User registerUser(User user);

    ResponseEntity<?> login(User user);
}
