package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.application.dto.InterestTxProjection;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.InterestTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaInterestTransactionRepository extends JpaRepository<InterestTransactionEntity, byte[]> {

    @Query("""
    SELECT
        it.txId          AS txId,
        it.earningId     AS earningId,
        it.date          AS date,
        it.amount        AS amount,
        it.createdAt     AS createdAt,
        s.subscriptionId AS subscriptionId
    FROM InterestTransactionEntity it
    JOIN EarningEntity e ON it.earningId = e.id
    JOIN e.subscription s
    WHERE s.walletId = :walletId
    ORDER BY it.createdAt DESC
""")
    List<InterestTxProjection> findAllByWalletId(@Param("walletId") UUID walletId);
}
