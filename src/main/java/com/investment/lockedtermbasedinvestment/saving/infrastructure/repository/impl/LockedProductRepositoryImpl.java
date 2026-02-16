package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.impl;

import com.investment.lockedtermbasedinvestment.common.enums.LockedProductStatus;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.LockedProductRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.LockedProductId;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.LockedProductEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaLockedProductRepository;
import com.investment.lockedtermbasedinvestment.saving.mapper.LockedProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public List<LockedProductAggregate> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(LockedProductMapper::toDomain)
                .toList();
    }

    @Override
    public List<LockedProductAggregate> findAllActive() {
        return jpaRepository.findByStatus(LockedProductStatus.ACTIVE)
                .stream()
                .map(LockedProductMapper::toDomain)
                .toList();
    }

    @Override
    public List<LockedProductAggregate> findAllOperating() {
        return jpaRepository
                .findByStatusIn(
                        List.of(
                                LockedProductStatus.ACTIVE,
                                LockedProductStatus.FULLED
                        )
                )
                .stream()
                .map(LockedProductMapper::toDomain)
                .toList();
    }
}
