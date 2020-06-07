package com.mm.moneymanager.model.Income;

import com.mm.moneymanager.model.Audit.DateAudit;
import com.mm.moneymanager.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Income extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private BigDecimal incomeAmount;

    private String incomeSource;

    private Boolean recurringIncome;

    private Boolean incomeArchived;

    private Integer durationOfRecurrence;

    private LocalDate paymentDate;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
