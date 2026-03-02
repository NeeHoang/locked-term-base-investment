package com.investment.lockedtermbasedinvestment.admin.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.admin.api.dto.response.LiquidityLedgerResponse;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityLedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaLiquidityLedgerRepository extends JpaRepository<LiquidityLedgerEntity, byte[]> {

    @Query("""
        SELECT new com.investment.lockedtermbasedinvestment.admin.api.dto.response.LiquidityLedgerResponse(
            l.amount, l.liquidityAfter
        )
        FROM LiquidityLedgerEntity l
        ORDER BY l.createdAt DESC
    """)
    List<LiquidityLedgerResponse> findAllOrderByCreatedAtDesc();
}
