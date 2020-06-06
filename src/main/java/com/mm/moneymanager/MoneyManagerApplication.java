package com.mm.moneymanager;

import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.RoleName;
import com.mm.moneymanager.model.user.User;
import com.mm.moneymanager.repository.RoleRepository;
import com.mm.moneymanager.repository.UserRepository;
import com.mm.moneymanager.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoneyManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyManagerApplication.class, args);
    }


    @Bean
    CommandLineRunner init(RoleRepository repository, UserRepository userRepository, UserService userService) {
        return args -> {
            if (repository.findByName(RoleName.ROLE_USER).isEmpty()) {
                repository.save(new Role(RoleName.ROLE_USER));
            }
            if (repository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
                repository.save(new Role(RoleName.ROLE_ADMIN));
            }
            if (userRepository.findAll().size() == 0) {
                userService.registerUser(
                        User.builder()
                                .firstName("test")
                                .password("password")
                                .email("test@test.com")
                                .surname("testsurname")
                                .username("test")
                                .build());

            }
        };
    }
}
