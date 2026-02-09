package com.investment.lockedtembasedinvestment.saving.infrastructure.persistence;

import com.investment.lockedtembasedinvestment.common.enums.EarningTransaction;
import com.investment.lockedtembasedinvestment.common.enums.EarningTxType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "earning_transactions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EarningTransactionEntity {

    @Id
    @Column(name = "tx_id", nullable = false, updatable = false, columnDefinition = "BYTEA")
    private byte[] txId; // ULID 16 bytes

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "earning_id", nullable = false)
    private EarningEntity earning;

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_type", nullable = false)
    private EarningTxType txType; // DAILY_INTEREST, EARLY_REDEEMED, REDEEMED

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EarningTransaction status; //PENDING, SUCCESS, FAILED

    @Column(name = "available_before", nullable = false, precision = 18, scale = 8)
    private BigDecimal availableBefore;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal amount;

    @Column(name = "available_after", nullable = false, precision = 18, scale = 8)
    private BigDecimal availableAfter;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}