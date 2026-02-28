package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.application.dto.EarningTxProjection;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaEarningTransactionRepository extends JpaRepository<EarningTransactionEntity, byte[]> {

    @Query("""
    SELECT
        et.txId            AS txId,
        et.earningId       AS earningId,
        et.txType          AS txType,
        et.status          AS status,
        et.availableBefore AS availableBefore,
        et.amount          AS amount,
        et.availableAfter  AS availableAfter,
        et.createdAt       AS createdAt,
        s.subscriptionId   AS subscriptionId
    FROM EarningTransactionEntity et
    JOIN EarningEntity e ON et.earningId = e.id
    JOIN e.subscription s
    WHERE s.walletId = :walletId
    ORDER BY et.createdAt DESC
""")
    List<EarningTxProjection> findAllByWalletId(@Param("walletId") UUID walletId);
}
