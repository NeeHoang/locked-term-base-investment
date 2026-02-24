package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaEarningRepository extends JpaRepository<EarningEntity, Long> {

    Optional<EarningEntity> findBySubscription_SubscriptionId(UUID subscriptionId);
}
