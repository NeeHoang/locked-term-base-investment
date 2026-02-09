package com.investment.lockedtembasedinvestment.saving.domain.repository;

import com.investment.lockedtembasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.SubscriptionId;

import java.util.Optional;

public interface SubscriptionRepository {

    Optional<SubscriptionAggregate> findById(SubscriptionId id);
    void save(SubscriptionAggregate aggregate);
}
