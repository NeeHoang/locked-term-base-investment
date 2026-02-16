package com.investment.lockedtermbasedinvestment.admin.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityLedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLiquidityLedgerRepository extends JpaRepository<LiquidityLedgerEntity, byte[]> {
}
