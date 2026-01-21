package com.investment.lockedtembasedinvestment.wallet.infrastructure.persistence;

import com.investment.lockedtembasedinvestment.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_wallets")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long walletId;

    @Column(name = "total_balance", nullable = false, precision = 18, scale = 8)
    private BigDecimal totalBalance;

    @Column(name = "balance_available", nullable = false, precision = 18, scale = 8)
    private BigDecimal balanceAvailable;

    @Column(name = "balance_frozen", nullable = false, precision = 18, scale = 8)
    private BigDecimal balanceFrozen;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WalletStatus status; // ACTIVE, INACTIVE, SUSPENDED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

