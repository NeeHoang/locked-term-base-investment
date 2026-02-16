package com.investment.lockedtermbasedinvestment.admin.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityPoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLiquidityPoolRepository extends JpaRepository<LiquidityPoolEntity, byte[]> {
}
