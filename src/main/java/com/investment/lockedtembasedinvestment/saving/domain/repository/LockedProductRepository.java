package com.investment.lockedtembasedinvestment.saving.domain.repository;

import com.investment.lockedtembasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.LockedProductId;

import java.util.Optional;

public interface LockedProductRepository {

    Optional<LockedProductAggregate> findById(LockedProductId id);
    void save(LockedProductAggregate lockedProduct);
}
