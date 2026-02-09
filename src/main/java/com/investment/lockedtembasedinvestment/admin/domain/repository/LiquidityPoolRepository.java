package com.investment.lockedtembasedinvestment.admin.domain.repository;

import com.investment.lockedtembasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;

public interface LiquidityPoolRepository {

    void save(LiquidityPoolAggregate aggregate);
}
