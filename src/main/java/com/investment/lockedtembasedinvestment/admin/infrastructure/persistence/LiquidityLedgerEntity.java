package com.investment.lockedtembasedinvestment.admin.infrastructure.persistence;

import com.investment.lockedtembasedinvestment.common.enums.LiquidityTransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "liquidity_ledger")
@Getter
@Setter
public class LiquidityLedgerEntity {

    @Id
    @Column(name = "tx_id", columnDefinition = "BYTEA", nullable = false, updatable = false)
    private byte[] txId; // ULID 16 byte

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_type", nullable = false, length = 50)
    private LiquidityTransactionType txType;

    @Column(name = "liquidity_before", precision = 18, scale = 8, nullable = false)
    private BigDecimal liquidityBefore;

    @Column(name = "amount", precision = 18, scale = 8, nullable = false)
    private BigDecimal amount;

    @Column(name = "liquidity_after", precision = 18, scale = 8, nullable = false)
    private BigDecimal liquidityAfter;

    @Column(name = "reference_id", columnDefinition = "BYTEA", nullable = false)
    private byte[] referenceId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }
}
