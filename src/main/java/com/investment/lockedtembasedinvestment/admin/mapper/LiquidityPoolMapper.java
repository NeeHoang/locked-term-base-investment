package com.investment.lockedtembasedinvestment.admin.mapper;

import com.investment.lockedtembasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtembasedinvestment.admin.domain.policy.LiquidityPoolStatusPolicy;
import com.investment.lockedtembasedinvestment.admin.domain.valueobject.LiquidityPoolId;
import com.investment.lockedtembasedinvestment.admin.infrastructure.persistence.LiquidityPoolEntity;
import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LiquidityPoolMapper {

    private final LiquidityPoolStatusPolicy statusPolicy;

    public LiquidityPoolMapper(
            LiquidityPoolStatusPolicy statusPolicy
    ) {
        this.statusPolicy = statusPolicy;
    }

    public LiquidityPoolAggregate toDomain(LiquidityPoolEntity entity) {
        return new LiquidityPoolAggregate(
                new LiquidityPoolId(entity.getId()),
                Money.of(entity.getTotalAmount()),
                Money.of(entity.getMinThreshold()),
                statusPolicy
        );
    }

    public LiquidityPoolEntity toEntity(LiquidityPoolAggregate aggregate) {
        LiquidityPoolEntity entity = new LiquidityPoolEntity();

        Instant now = Instant.now();

        entity.setId(aggregate.getId().value());
        entity.setTotalAmount(aggregate.getTotalAmount().amount());
        entity.setMinThreshold(aggregate.getMinThreshold().amount());
        entity.setStatus(aggregate.getStatus());
        entity.setLastInjectedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }
}
