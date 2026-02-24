package com.investment.lockedtermbasedinvestment.saving.domain.repository;

import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.LockedProductId;

import java.util.List;
import java.util.Optional;

public interface LockedProductRepository {

    Optional<LockedProductAggregate> findById(LockedProductId id);

    void save(LockedProductAggregate lockedProduct);

    void update(LockedProductAggregate lockedProduct);

    List<LockedProductAggregate> findAll();

    List<LockedProductAggregate> findAllActive();

    List<LockedProductAggregate> findAllOperating();
}
