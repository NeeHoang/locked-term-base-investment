package com.investment.lockedtembasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.LockedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockedProductRepository extends JpaRepository<LockedProductEntity, Long> {
}
