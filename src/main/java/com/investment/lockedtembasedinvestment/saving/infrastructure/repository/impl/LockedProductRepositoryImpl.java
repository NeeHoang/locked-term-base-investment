package com.investment.lockedtembasedinvestment.saving.infrastructure.repository.impl;

import com.investment.lockedtembasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.repository.LockedProductRepository;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.LockedProductId;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.LockedProductEntity;
import com.investment.lockedtembasedinvestment.saving.infrastructure.repository.JpaLockedProductRepository;
import com.investment.lockedtembasedinvestment.saving.mapper.LockedProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LockedProductRepositoryImpl implements LockedProductRepository {

    private final JpaLockedProductRepository jpaRepository;

    @Override
    public Optional<LockedProductAggregate> findById(LockedProductId id) {
        return jpaRepository.findById(id.value())
                .map(LockedProductMapper::toDomain);
    }

    @Override
    public void save(LockedProductAggregate aggregate) {

        LockedProductEntity entity = LockedProductMapper.toEntity(aggregate);

        LockedProductEntity saved = jpaRepository.save(entity);

        if (aggregate.getId() == null) {
            aggregate.assignId(
                    new LockedProductId(saved.getProductId())
            );
        }
    }
}
