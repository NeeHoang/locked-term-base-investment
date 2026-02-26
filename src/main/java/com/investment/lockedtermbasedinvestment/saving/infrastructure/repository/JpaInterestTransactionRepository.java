package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.InterestTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaInterestTransactionRepository extends JpaRepository<InterestTransactionEntity, byte[]> {

    @Query("""
        SELECT it FROM InterestTransactionEntity it
        JOIN EarningEntity e ON it.earningId = e.id
        JOIN e.subscription s
        WHERE s.walletId = :walletId
        ORDER BY it.createdAt DESC
    """)
    List<InterestTransactionEntity> findAllByWalletId(
            @Param("walletId") UUID walletId
    );
}
