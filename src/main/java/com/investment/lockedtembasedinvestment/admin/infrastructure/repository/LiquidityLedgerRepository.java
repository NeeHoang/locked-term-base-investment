package com.investment.lockedtembasedinvestment.admin.infrastructure.repository;

import com.investment.lockedtembasedinvestment.admin.infrastructure.persistence.LiquidityLedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquidityLedgerRepository extends JpaRepository<LiquidityLedgerEntity, byte[]> {
}
