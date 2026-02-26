package com.investment.lockedtermbasedinvestment.admin.domain.repository;

import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;

import java.util.List;

public interface LiquidityPoolRepository {

    void save(LiquidityPoolAggregate aggregate);

    List<LiquidityPoolAggregate> findAll();

    LiquidityPoolAggregate findSolo();
}
