package com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "withdraw_transactions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawTransactionEntity {

    @Id
    @Column(name = "tx_id", nullable = false, updatable = false, columnDefinition = "BYTEA")
    private byte[] txId; // ULID 16 bytes

    @Column(name = "earning_id", nullable = false)
    private Long earningId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "available_before", nullable = false, precision = 18, scale = 8)
    private BigDecimal availableBefore;

    @Column(name = "available_after", nullable = false, precision = 18, scale = 8)
    private BigDecimal availableAfter;

    @Column(nullable = false, precision = 18, scale = 8)
    private BigDecimal amount;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public static WithdrawTransactionEntity withdraw(
            Long earningId,
            BigDecimal before,
            BigDecimal amount,
            BigDecimal after
    ) {
        WithdrawTransactionEntity tx = new WithdrawTransactionEntity();
        tx.setTxId(UlidCreator.getUlid().toBytes());
        tx.setEarningId(earningId);
        tx.setDate(LocalDate.now());
        tx.setAvailableBefore(before);
        tx.setAmount(amount);
        tx.setAvailableAfter(after);
        return tx;
    }
}
