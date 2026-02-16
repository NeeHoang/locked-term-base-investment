package com.investment.lockedtermbasedinvestment.admin.application.service.impl;

import com.investment.lockedtermbasedinvestment.admin.api.dto.request.LiquidityPoolRequest;
import com.investment.lockedtermbasedinvestment.admin.application.service.LiquidityPoolService;
import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtermbasedinvestment.admin.domain.factory.LiquidityPoolFactory;
import com.investment.lockedtermbasedinvestment.admin.domain.repository.LiquidityPoolRepository;
import com.investment.lockedtermbasedinvestment.admin.domain.valueobject.LiquidityPoolId;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<LiquidityPoolAggregate> getAll() {
        return liquidityPoolRepository.findAll();
    }
}
