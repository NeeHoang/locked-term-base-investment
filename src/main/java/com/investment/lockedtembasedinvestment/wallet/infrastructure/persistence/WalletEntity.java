package com.investment.lockedtembasedinvestment.wallet.infrastructure.persistence;

import com.investment.lockedtembasedinvestment.common.enums.WalletStatus;
import com.investment.lockedtembasedinvestment.common.persistence.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "user_wallets")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity extends AuditableEntity {

    @Id
    @Column(name = "wallet_id", updatable = false, nullable = false)
    private UUID walletId;

    @Column(name = "total_balance", nullable = false, precision = 18, scale = 8)
    private BigDecimal totalBalance;

    @Column(name = "balance_available", nullable = false, precision = 18, scale = 8)
    private BigDecimal balanceAvailable;

    @Column(name = "balance_frozen", nullable = false, precision = 18, scale = 8)
    private BigDecimal balanceFrozen;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WalletStatus status; // ACTIVE, INACTIVE, SUSPENDED
}

