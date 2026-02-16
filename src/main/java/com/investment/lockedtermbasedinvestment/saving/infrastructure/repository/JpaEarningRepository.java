package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEarningRepository extends JpaRepository<EarningEntity, Long> {
}
