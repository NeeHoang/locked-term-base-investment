package com.investment.lockedtembasedinvestment.admin.infrastructure.repository;

import com.investment.lockedtembasedinvestment.admin.infrastructure.persistence.AdminInjectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInjectionRepository extends JpaRepository<AdminInjectionEntity, byte[]> {
}
