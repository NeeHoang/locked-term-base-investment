package com.investment.lockedtermbasedinvestment.saving.domain.repository;

import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.EarningId;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.SubscriptionId;

import java.util.Optional;

public interface EarningRepository {

    EarningAggregate save(EarningAggregate earningAggregate);

    Optional<EarningAggregate> findById(EarningId id);

    Optional<EarningAggregate> findBySubscriptionId(SubscriptionId subscriptionId);

    void update(EarningAggregate earning);

}
