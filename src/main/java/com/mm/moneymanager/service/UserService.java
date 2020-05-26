package com.mm.moneymanager.service;


import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.model.user.UserDTO;
import com.mm.moneymanager.model.user.UserLogin;

import java.util.List;

public interface UserService {
    List<String> checkEmailExists(String email);

    List<String> checkUsernameExists(String username);

    User registerUser(User user);

    String login(UserLogin user);

    UserDTO returnUser(String username);
}
