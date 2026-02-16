package com.investment.lockedtermbasedinvestment.saving.domain.repository;

import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.SubscriptionId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {

    Optional<SubscriptionAggregate> findById(SubscriptionId id);
    void save(SubscriptionAggregate aggregate);

    List<SubscriptionAggregate> findProductSubscribeToday();

    List<SubscriptionAggregate> findHistorySubscribe(UUID walletId);
}
