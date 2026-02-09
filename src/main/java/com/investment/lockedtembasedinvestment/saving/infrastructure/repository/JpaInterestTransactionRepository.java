package com.investment.lockedtembasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.InterestTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaInterestTransactionRepository extends JpaRepository<InterestTransactionEntity, byte[]> {
}
