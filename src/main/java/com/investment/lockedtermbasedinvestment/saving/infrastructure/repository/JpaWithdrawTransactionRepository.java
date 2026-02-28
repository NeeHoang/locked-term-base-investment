package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.application.dto.WithdrawTxProjection;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.WithdrawTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaWithdrawTransactionRepository extends JpaRepository<WithdrawTransactionEntity, byte[]> {

    @Query("""
    SELECT
        wt.txId            AS txId,
        wt.earningId       AS earningId,
        wt.date            AS date,
        wt.availableBefore AS availableBefore,
        wt.amount          AS amount,
        wt.availableAfter  AS availableAfter,
        wt.createdAt       AS createdAt,
        s.subscriptionId   AS subscriptionId
    FROM WithdrawTransactionEntity wt
    JOIN EarningEntity e ON wt.earningId = e.id
    JOIN e.subscription s
    WHERE s.walletId = :walletId
    ORDER BY wt.createdAt DESC
""")
    List<WithdrawTxProjection> findAllByWalletId(@Param("walletId") UUID walletId);
}
