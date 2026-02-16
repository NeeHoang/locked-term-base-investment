package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

    List<SubscriptionEntity> findByStartDate(LocalDate startDate);

    List<SubscriptionEntity> findByWalletId(UUID walletId);
}
