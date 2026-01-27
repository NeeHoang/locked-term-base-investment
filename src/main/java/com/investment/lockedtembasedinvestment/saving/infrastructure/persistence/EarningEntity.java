package com.investment.lockedtembasedinvestment.saving.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "earnings")
@Getter
@Setter
@NoArgsConstructor
public class EarningEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "earning_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private SubscriptionEntity subscriptionId;

    @Column(name = "principal", precision = 18, scale = 8, nullable = false)
    private BigDecimal principal;

    @Column(name = "available", precision = 18, scale = 8, nullable = false)
    private BigDecimal available;

    @Column(name = "total_interest", precision = 18, scale = 8, nullable = false)
    private BigDecimal totalInterest;

    @Column(name = "interest_per_day", precision = 18, scale = 8, nullable = false)
    private BigDecimal interestPerDay;

    @Column(name = "progress", precision = 5, scale = 2, nullable = false)
    private BigDecimal progress;

    @Column(name = "penalty_rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal penaltyRate;

    @Column(name = "penalty_amount", precision = 5, scale = 2, nullable = false)
    private BigDecimal penaltyAmount;

    @Column(name = "holding_days", nullable = false)
    private int holdingDays;

    @Column(name = "term_days", nullable = false)
    private int termDays;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}
