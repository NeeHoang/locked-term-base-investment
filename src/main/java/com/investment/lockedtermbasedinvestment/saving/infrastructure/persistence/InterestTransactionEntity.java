package com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence;

import com.github.f4b6a3.ulid.UlidCreator;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(
        name = "interest_transactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_interest_tx_earning_date",
                        columnNames = {"earning_id", "date"}
                )
        }
)
@Getter @Setter
public class InterestTransactionEntity {

    @Id
    @Column(name = "tx_id", nullable = false, updatable = false, columnDefinition = "BYTEA")
    private byte[] txId; // ULID 16 bytes

    @JoinColumn(name = "earning_id", nullable = false)
    private Long earningId;

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

    public static InterestTransactionEntity dailyInterest(
            Long earningId,
            LocalDate date,
            Money amount
    ) {
        InterestTransactionEntity tx = new InterestTransactionEntity();
        tx.setTxId(UlidCreator.getUlid().toBytes());
        tx.setEarningId(earningId);
        tx.setDate(date);
        tx.setAmount(amount.toBigDecimal());
        return tx;
    }
}
