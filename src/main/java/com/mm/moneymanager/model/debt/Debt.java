package com.mm.moneymanager.model.debt;

import com.mm.moneymanager.model.Audit.UserDateAudit;
import com.mm.moneymanager.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Debt extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String company;
    BigInteger amount;

    LocalDate dueDate;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
