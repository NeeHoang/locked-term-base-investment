package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.api.dto.response.SumInterestPerDay;
import com.investment.lockedtermbasedinvestment.saving.application.dto.EarningListProjection;
import com.investment.lockedtermbasedinvestment.saving.application.dto.EarningSummaryProjection;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaEarningRepository extends JpaRepository<EarningEntity, Long> {

    Optional<EarningEntity> findBySubscription_SubscriptionId(UUID subscriptionId);

    @Query("""
       SELECT SUM(e.available) AS totalAvailable,
              SUM(e.totalInterest - COALESCE(e.penaltyAmount, 0)) AS totalInterest
       FROM EarningEntity e
       JOIN e.subscription s
       WHERE s.walletId = :walletId
       """)
    EarningSummaryProjection sumEarningsByWalletId(@Param("walletId") UUID walletId);

    @Query("""
    SELECT
        e.id            AS earningId,
        e.termDays      AS termDays,
        e.principal     AS principal,
        e.available     AS availableToWithdraw,
        e.totalInterest AS accruedInterest,
        e.holdingDays   AS holdingDays,
        e.progress      AS progress
    FROM EarningEntity e
    JOIN e.subscription s
    WHERE s.walletId = :walletId
        AND e.available > 0
    ORDER BY e.id DESC
""")
    List<EarningListProjection> findAllByWalletId(@Param("walletId") UUID walletId);

    @Query("""
        SELECT SUM(e.interestPerDay)
        FROM EarningEntity e
    """)
    SumInterestPerDay findSumInterestPerDay();
}
