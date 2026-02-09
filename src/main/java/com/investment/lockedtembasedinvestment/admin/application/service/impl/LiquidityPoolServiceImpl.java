package com.investment.lockedtembasedinvestment.admin.application.service.impl;

import com.investment.lockedtembasedinvestment.admin.api.dto.request.LiquidityPoolRequest;
import com.investment.lockedtembasedinvestment.admin.application.service.LiquidityPoolService;
import com.investment.lockedtembasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtembasedinvestment.admin.domain.factory.LiquidityPoolFactory;
import com.investment.lockedtembasedinvestment.admin.domain.repository.LiquidityPoolRepository;
import com.investment.lockedtembasedinvestment.admin.domain.valueobject.LiquidityPoolId;
import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LiquidityPoolServiceImpl implements LiquidityPoolService {

    private final LiquidityPoolRepository liquidityPoolRepository;
    private final LiquidityPoolFactory factory;

    @Override
    public void createPool(LiquidityPoolRequest request) {

        LiquidityPoolAggregate aggregate =
                factory.createNew(
                        LiquidityPoolId.generate(),
                        Money.zero(),
                        Money.of(request.minThreshold())
                );
        log.info("LiquidityPool Id: {}", aggregate.getId().value());
        liquidityPoolRepository.save(aggregate);
    }
}
