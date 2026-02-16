package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEarningTransactionRepository extends JpaRepository<EarningTransactionEntity, byte[]> {
}
