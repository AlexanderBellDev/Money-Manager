package com.mm.moneymanager.service;


import com.mm.moneymanager.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<String> checkEmailExists(String email);
    List<String> checkUsernameExists(String username);
}
