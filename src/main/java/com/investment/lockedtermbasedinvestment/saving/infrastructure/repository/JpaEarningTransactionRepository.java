package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.common.enums.EarningTransaction;
import com.investment.lockedtermbasedinvestment.common.enums.EarningTxType;
import com.investment.lockedtermbasedinvestment.saving.application.dto.EarningTxProjection;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaEarningTransactionRepository extends JpaRepository<EarningTransactionEntity, byte[]> {

    @Query("""
    SELECT et
    FROM EarningTransactionEntity et
    WHERE et.earningId = :earningId
      AND et.txType = :txType
      AND et.status = :status
    ORDER BY et.createdAt DESC
    LIMIT 1
""")
    Optional<EarningTransactionEntity> findLatestPending(
            @Param("earningId") Long earningId,
            @Param("txType") EarningTxType txType,
            @Param("status") EarningTransaction status
    );

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
    AND et.status != "FAILED"
    ORDER BY et.createdAt DESC
""")
    List<EarningTxProjection> findAllByWalletId(@Param("walletId") UUID walletId);

    @Modifying
    @Query("UPDATE EarningTransactionEntity e SET e.status = :status WHERE e.txId = :txId")
    void updateStatus(
            @Param("txId") byte[] txId,
            @Param("status") EarningTransaction status
    );

    @Modifying
    @Query("""
    UPDATE EarningTransactionEntity e
    SET e.status = :status,
        e.amount = :amount,
        e.availableAfter = :availableAfter
    WHERE e.txId = :txId
""")
    void updateSuccessStatus(
            @Param("txId") byte[] txId,
            @Param("status") EarningTransaction status,
            @Param("amount") BigDecimal amount,
            @Param("availableAfter") BigDecimal availableAfter
    );
}
