package com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.impl;

import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtermbasedinvestment.admin.domain.repository.LiquidityPoolRepository;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityPoolEntity;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaLiquidityPoolRepository;
import com.investment.lockedtermbasedinvestment.admin.mapper.LiquidityPoolMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LiquidityPoolRepositoryImpl implements LiquidityPoolRepository {

    private final JpaLiquidityPoolRepository jpaLiquidityPoolRepository;
    private final LiquidityPoolMapper mapper;

    @Override
    public void save(LiquidityPoolAggregate aggregate) {
        LiquidityPoolEntity entity = mapper.toEntity(aggregate);
        jpaLiquidityPoolRepository.save(entity);
        log.info("Create Liquidity Pool successfully with Id: {}", aggregate.getId().value());
    }

    @Override
    public List<LiquidityPoolAggregate> findAll() {
        return jpaLiquidityPoolRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
