package com.investment.lockedtermbasedinvestment.admin.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.AdminInjectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAdminInjectionRepository extends JpaRepository<AdminInjectionEntity, byte[]> {
}
