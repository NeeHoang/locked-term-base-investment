package com.investment.lockedtermbasedinvestment.admin.application.service;

import com.investment.lockedtermbasedinvestment.admin.api.dto.request.LiquidityPoolRequest;
import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;

import java.util.List;

public interface LiquidityPoolService {

    void createPool(LiquidityPoolRequest request);

    List<LiquidityPoolAggregate> getAll();

}
