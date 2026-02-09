package com.investment.lockedtembasedinvestment.admin.application.service;

import com.investment.lockedtembasedinvestment.admin.api.dto.request.LiquidityPoolRequest;

public interface LiquidityPoolService {

    void createPool(LiquidityPoolRequest request);
}
