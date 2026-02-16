package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository;

import com.investment.lockedtermbasedinvestment.common.enums.LockedProductStatus;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.LockedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaLockedProductRepository extends JpaRepository<LockedProductEntity, Long> {

    List<LockedProductEntity> findByStatus(LockedProductStatus status);

    List<LockedProductEntity> findByStatusIn(List<LockedProductStatus> statuses);

}
