package com.investment.lockedtermbasedinvestment.saving.mapper;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.*;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;

import java.math.BigDecimal;

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
                entity.getPenaltyRate() == null
                        || entity.getPenaltyRate().compareTo(BigDecimal.ZERO) == 0
                        ? PenaltyRate.notAllowed()
                        : PenaltyRate.of(entity.getPenaltyRate()),
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
        entity.setTermDays(aggregate.getTermDays().value());
        entity.setPenaltyRate(aggregate.getPenaltyRate().value());
        entity.setProgress(aggregate.getProgress().value());

        return entity;
    }
}
