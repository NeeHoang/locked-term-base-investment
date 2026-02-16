package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.InterestTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInterestTransactionRepository extends JpaRepository<InterestTransactionEntity, byte[]> {
}
