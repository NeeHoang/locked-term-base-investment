package com.investment.lockedtembasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.LockedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLockedProductRepository extends JpaRepository<LockedProductEntity, Long> {
}
