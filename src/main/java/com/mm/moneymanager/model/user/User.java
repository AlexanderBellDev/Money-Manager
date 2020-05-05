package com.mm.moneymanager.model.user;

import com.mm.moneymanager.model.Audit.DateAudit;
import com.mm.moneymanager.model.Role;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String username;
    String firstName;
    String surname;
    String email;
    String password;


    @ManyToMany
    private Set<Role> roles;

}
