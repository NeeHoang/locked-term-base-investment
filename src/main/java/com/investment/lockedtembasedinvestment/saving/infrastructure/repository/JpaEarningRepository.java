package com.investment.lockedtembasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.EarningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEarningRepository extends JpaRepository<EarningEntity, Long> {
}
