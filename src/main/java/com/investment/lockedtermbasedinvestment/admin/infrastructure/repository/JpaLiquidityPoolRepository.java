package com.investment.lockedtermbasedinvestment.admin.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityPoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaLiquidityPoolRepository extends JpaRepository<LiquidityPoolEntity, byte[]> {

    @Query(value = "select * from liquidity_pool limit 1", nativeQuery = true)
    LiquidityPoolEntity findSoloRecord();
}
