package com.mm.moneymanager.service;


import com.mm.moneymanager.model.User;

import java.util.List;

public interface UserService {
    boolean saveUser(User user);

    List<String> checkEmailExists(String email);
    List<String> checkUsernameExists(String username);
    boolean checkLogin(String username, String password);
}
