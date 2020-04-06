package com.mm.moneymanager.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String username;
    String firstName;
    String surname;
    String email;
    String password;


    @ManyToMany
    private Set<Role> roles = new HashSet<>();

}
