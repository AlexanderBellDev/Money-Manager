package com.mm.moneymanager.service.Impl;


import com.mm.moneymanager.service.UserService;
import com.mm.moneymanager.model.User;
import com.mm.moneymanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean saveUser(User user) {

        userRepository.save(user);
        return true;
    }

    public List<String> checkEmailExists(String email) {
        if (userRepository.findAllByEmail(email).isEmpty()) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList("exists");
        }
    }

    public List<String> checkUsernameExists(String username) {
        System.out.println(userRepository.findAllByUsername(username));
        if (userRepository.findAllByUsername(username).isEmpty()) {
            return Collections.emptyList();
        } else {
            return Collections.singletonList("exists");
        }
    }

    public boolean checkLogin(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password) != null;
    }
}

