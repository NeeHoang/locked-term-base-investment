package com.investment.lockedtembasedinvestment.admin.infrastructure.repository;

import com.investment.lockedtembasedinvestment.admin.infrastructure.persistence.LiquidityPoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLiquidityPoolRepository extends JpaRepository<LiquidityPoolEntity, byte[]> {
}
