package com.investment.lockedtembasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEarningTransactionRepository extends JpaRepository<EarningTransactionEntity, byte[]> {
}
