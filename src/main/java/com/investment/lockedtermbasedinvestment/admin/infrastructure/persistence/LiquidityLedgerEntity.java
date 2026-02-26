package com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence;

import com.github.f4b6a3.ulid.UlidCreator;
import com.investment.lockedtermbasedinvestment.common.enums.LiquidityTransactionType;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
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

    public static LiquidityLedgerEntity dailyInterestDebit(
            Money liquidityBefore,
            Money debitAmount,
            Money liquidityAfter,
            byte[] earningTxId
    ) {

        LiquidityLedgerEntity ledger = new LiquidityLedgerEntity();

        ledger.setTxId(UlidCreator.getUlid().toBytes());
        ledger.setTxType(LiquidityTransactionType.DAILY_INTEREST);

        ledger.setLiquidityBefore(liquidityBefore.toBigDecimal());
        ledger.setAmount(debitAmount.toBigDecimal());
        ledger.setLiquidityAfter(liquidityAfter.toBigDecimal());

        ledger.setReferenceId(earningTxId);

        return ledger;
    }

    public static LiquidityLedgerEntity redemptionDebit(
            Money liquidityBefore,
            Money amount,
            Money liquidityAfter,
            byte[] earningTransactionId
    ) {
        LiquidityLedgerEntity ledger = new LiquidityLedgerEntity();
        ledger.setTxId(UlidCreator.getUlid().toBytes());
        ledger.setTxType(LiquidityTransactionType.REDEEMED);
        ledger.setLiquidityBefore(liquidityBefore.toBigDecimal());
        ledger.setAmount(amount.toBigDecimal());
        ledger.setLiquidityAfter(liquidityAfter.toBigDecimal());
        ledger.setReferenceId(earningTransactionId);
        return ledger;
    }

    public static LiquidityLedgerEntity EarlyRedeemDebit(
            Money liquidityBefore,
            Money amount,
            Money liquidityAfter,
            byte[] earningTransactionId
    ) {
        LiquidityLedgerEntity ledger = new LiquidityLedgerEntity();
        ledger.setTxId(UlidCreator.getUlid().toBytes());
        ledger.setTxType(LiquidityTransactionType.EARLY_REDEEMED);
        ledger.setLiquidityBefore(liquidityBefore.toBigDecimal());
        ledger.setAmount(amount.toBigDecimal());
        ledger.setLiquidityAfter(liquidityAfter.toBigDecimal());
        ledger.setReferenceId(earningTransactionId);
        return ledger;
    }
}
