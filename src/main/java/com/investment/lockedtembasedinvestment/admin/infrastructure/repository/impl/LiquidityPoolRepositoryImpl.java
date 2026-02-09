package com.investment.lockedtembasedinvestment.admin.infrastructure.repository.impl;

import com.investment.lockedtembasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtembasedinvestment.admin.domain.repository.LiquidityPoolRepository;
import com.investment.lockedtembasedinvestment.admin.infrastructure.persistence.LiquidityPoolEntity;
import com.investment.lockedtembasedinvestment.admin.infrastructure.repository.JpaLiquidityPoolRepository;
import com.investment.lockedtembasedinvestment.admin.mapper.LiquidityPoolMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LiquidityPoolRepositoryImpl implements LiquidityPoolRepository {

    private final JpaLiquidityPoolRepository jpaLiquidityPoolRepository;
    private final LiquidityPoolMapper mapper;

    @Override
    public void save(LiquidityPoolAggregate aggregate) {
        LiquidityPoolEntity entity = mapper.toEntity(aggregate);
        log.info("Create Liquidity Pool successfully with Id: {}", aggregate.getId().value());
        jpaLiquidityPoolRepository.save(entity);
    }
}
