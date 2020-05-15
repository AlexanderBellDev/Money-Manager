package com.mm.moneymanager.model.user;

import com.mm.moneymanager.model.Audit.DateAudit;
import com.mm.moneymanager.model.Role;
import com.mm.moneymanager.model.debt.Debt;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
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


    @OneToMany(mappedBy = "user")
    private Set<Debt> debts;

}
