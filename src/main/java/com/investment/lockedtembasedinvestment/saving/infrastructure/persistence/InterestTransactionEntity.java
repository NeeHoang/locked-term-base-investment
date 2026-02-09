package com.investment.lockedtembasedinvestment.saving.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "interest_transactions")
@Getter @Setter
public class InterestTransactionEntity {

    @Id
    @Column(name = "tx_id", nullable = false, updatable = false, columnDefinition = "BYTEA")
    private byte[] txId; // ULID 16 bytes

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "earning_id", nullable = false)
    private EarningEntity earning;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal amount;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
