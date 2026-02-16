package com.investment.lockedtermbasedinvestment.admin.domain.factory;

import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtermbasedinvestment.admin.domain.policy.LiquidityPoolStatusPolicy;
import com.investment.lockedtermbasedinvestment.admin.domain.valueobject.LiquidityPoolId;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;

public class LiquidityPoolFactory {

    private final LiquidityPoolStatusPolicy statusPolicy;

    public LiquidityPoolFactory(LiquidityPoolStatusPolicy statusPolicy) {
        this.statusPolicy = statusPolicy;
    }

    public LiquidityPoolAggregate createNew(
            LiquidityPoolId id,
            Money initialAmount,
            Money minThreshold
    ) {
        return new LiquidityPoolAggregate(
                id,
                initialAmount,
                minThreshold,
                statusPolicy
        );
    }

    // Load DB
    public LiquidityPoolAggregate rehydrate(
            LiquidityPoolId id,
            Money totalAmount,
            Money minThreshold
    ) {
        return new LiquidityPoolAggregate(
                id,
                totalAmount,
                minThreshold,
                statusPolicy
        );
    }
}
