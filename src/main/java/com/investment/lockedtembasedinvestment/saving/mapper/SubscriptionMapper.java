package com.investment.lockedtembasedinvestment.saving.mapper;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.*;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.LockedProductEntity;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;

public class SubscriptionMapper {

    public static SubscriptionAggregate toDomain(SubscriptionEntity entity) {
        return new SubscriptionAggregate(
                new SubscriptionId(entity.getSubscriptionId()),
                new WalletRef(entity.getWalletId()),
                new LockedProductId(entity.getProduct().getProductId()),
                Money.of(entity.getPrincipal()),
                new InterestRate(entity.getProduct().getInterestRate()),
                new TermDays(entity.getProduct().getTermDays()),
                entity.getStartDate()
        );
    }

    public static SubscriptionEntity toEntity(SubscriptionAggregate aggregate,
                                              LockedProductEntity productEntity) {

        SubscriptionEntity entity = new SubscriptionEntity();

        if (aggregate.getId() != null) {
            entity.setSubscriptionId(aggregate.getId().value());
        }

        entity.setWalletId(aggregate.getWalletRef().value());
        entity.setProduct(productEntity);
        entity.setPrincipal(aggregate.getPrincipal().amount());
        entity.setTotalInterest(aggregate.getTotalInterest().amount());
        entity.setStartDate(aggregate.getStartDate());
        entity.setMaturityDate(aggregate.getMaturityDate());
        entity.setStatus(aggregate.getStatus());

        return entity;
    }
}
