package com.investment.lockedtembasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {
}
