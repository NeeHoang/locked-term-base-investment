package com.investment.lockedtembasedinvestment.saving.mapper;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.*;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.EarningEntity;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;

public class EarningMapper {

    public static EarningAggregate toDomain(EarningEntity entity) {
        return new EarningAggregate(
                new EarningId(entity.getId()),
                new SubscriptionId(entity.getSubscription().getSubscriptionId()),
                Money.of(entity.getPrincipal()),
                new TermDays(entity.getTermDays()),
                Money.of(entity.getInterestPerDay()),
                new HoldingDays(entity.getHoldingDays()),
                new Progress(entity.getProgress()),
                Money.of(entity.getTotalInterest()),
                Money.of(entity.getAvailable()),
                new PenaltyRate(entity.getPenaltyRate()),
                Money.of(entity.getPenaltyAmount())
        );
    }

    public static EarningEntity toEntity(EarningAggregate aggregate,
                                         SubscriptionEntity subscription) {
        EarningEntity entity = new EarningEntity();

        if (aggregate.getId() != null) {
            entity.setId(aggregate.getId().value());
        }
        entity.setSubscription(subscription);
        entity.setPrincipal(aggregate.getPrincipal().amount());
        entity.setTotalInterest(aggregate.getTotalInterest().amount());
        entity.setAvailable(aggregate.getAvailable().amount());
        entity.setHoldingDays(aggregate.getHoldingDays().value());
        entity.setInterestPerDay(aggregate.getInterestPerDay().amount());
        entity.setPenaltyAmount(aggregate.getPenaltyAmount().amount());
        entity.setPenaltyRate(aggregate.getPenaltyRate().value());
        entity.setProgress(aggregate.getProgress().value());

        return entity;
    }
}
