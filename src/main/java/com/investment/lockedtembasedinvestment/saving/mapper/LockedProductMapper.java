package com.investment.lockedtembasedinvestment.saving.mapper;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.InterestRate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.LockedProductId;

import com.investment.lockedtembasedinvestment.saving.domain.valueobject.TermDays;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.LockedProductEntity;

public class LockedProductMapper {

    public static LockedProductAggregate toDomain(LockedProductEntity entity) {
        return new LockedProductAggregate(
                new LockedProductId(entity.getProductId()),
                new TermDays(entity.getTermDays()),
                new InterestRate(entity.getInterestRate()),
                Money.of(entity.getMinAmount()),
                Money.of(entity.getMaxAmount()),
                Money.of(entity.getTotalQuota()),
                entity.getDescription()
        );
    }

    public static LockedProductEntity toEntity(LockedProductAggregate aggregate) {

        LockedProductEntity entity = new LockedProductEntity();

        if (aggregate.getId() != null) {
            entity.setProductId(aggregate.getId().value());
        }

        entity.setTermDays(aggregate.getTermDays().value());
        entity.setInterestRate(aggregate.getInterestRate().value());
        entity.setMinAmount(aggregate.getMinAmount().amount());
        entity.setMaxAmount(aggregate.getMaxAmount().amount());
        entity.setTotalQuota(aggregate.getTotalQuota().amount());
        entity.setAvailableQuota(aggregate.getAvailableQuota().amount());
        entity.setDescription(aggregate.getDescription());
        entity.setStatus(aggregate.getStatus());

        return entity;
    }
}
