package com.mm.moneymanager.model.Income;

import com.mm.moneymanager.model.Audit.DateAudit;
import com.mm.moneymanager.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Income extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Version
    Long version;

    BigDecimal incomeAmount;

    String incomeSource;

    Boolean recurringIncome;

    Boolean incomeArchived;

    Integer durationOfRecurrence;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
