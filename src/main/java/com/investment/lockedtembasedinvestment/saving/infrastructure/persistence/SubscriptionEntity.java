package com.investment.lockedtembasedinvestment.saving.infrastructure.persistence;

import com.investment.lockedtembasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtembasedinvestment.common.persistence.AuditableEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter @Setter
public class SubscriptionEntity extends AuditableEntity {

    @Id
    @Column(name = "subscription_id")
    private UUID subscriptionId;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId; // Chỉ lưu ID vì Wallet thuộc Bounded Context khác

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private LockedProductEntity product;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal principal;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status; // ACTIVE, MATURED, EARLY_REDEEMED

    @Column(name = "total_interest", precision = 18, scale = 8)
    private BigDecimal totalInterest;
}

