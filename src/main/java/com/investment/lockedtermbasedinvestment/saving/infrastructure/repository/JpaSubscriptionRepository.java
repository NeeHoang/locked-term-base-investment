package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.ActivePackageResponse;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

    List<SubscriptionEntity> findByStartDate(
            @Param("startDate") LocalDate startDate
    );

    List<SubscriptionEntity> findByWalletId(UUID walletId);

    List<SubscriptionEntity> findByStatus(SubscriptionStatus status);

    @Query("""
    select s
    from SubscriptionEntity s
    where s.walletId = :walletId
    and s.startDate = :startDate
""")
    List<SubscriptionEntity> findByWalletIdAndStartDate(
            @Param("walletId") UUID walletId,
            @Param("startDate") LocalDate startDate
    );

    @Query("""
    select s
    from SubscriptionEntity s
    where s.walletId = :walletId
    and s.status = :status
""")
    List<SubscriptionEntity> findByWalletIdAndActive(
            @Param("walletId") UUID walletId,
            @Param("status") SubscriptionStatus status
    );

    @Query("""
select new com.investment.lockedtermbasedinvestment.saving.api.dto.response.ActivePackageResponse(
    s.subscriptionId,
    e.id,
    p.id,
    s.startDate,
    s.maturityDate,
    s.principal,
    p.interestRate,
    e.totalInterest,
    e.holdingDays,
    e.progress
)
from SubscriptionEntity s
join s.product p
join EarningEntity e on e.subscription = s
where s.walletId = :walletId
  and s.status = :status
""")
    List<ActivePackageResponse> findActivePackageByWalletId(
            UUID walletId,
            SubscriptionStatus status
    );
}