package com.investment.lockedtembasedinvestment.saving.domain.repository;

import com.investment.lockedtembasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.EarningId;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.SubscriptionId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EarningRepository {

    Optional<EarningAggregate> findById(EarningId id);
    Optional<EarningAggregate> findBySubscriptionId(SubscriptionId subscriptionId);
    void save(EarningAggregate earning);
    List<EarningAggregate> findAllActiveForAccrual(LocalDate today);
}
