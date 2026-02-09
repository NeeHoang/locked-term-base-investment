package com.investment.lockedtembasedinvestment.saving.infrastructure.repository.impl;

import com.investment.lockedtembasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.repository.SubscriptionRepository;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.SubscriptionId;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.LockedProductEntity;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;
import com.investment.lockedtembasedinvestment.saving.infrastructure.repository.JpaLockedProductRepository;
import com.investment.lockedtembasedinvestment.saving.infrastructure.repository.JpaSubscriptionRepository;
import com.investment.lockedtembasedinvestment.saving.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final JpaSubscriptionRepository jpaRepository;
    private final JpaLockedProductRepository lockedProductRepository;

    @Override
    public Optional<SubscriptionAggregate> findById(SubscriptionId id) {
        return jpaRepository.findById(id.value())
                .map(SubscriptionMapper::toDomain);
    }

    @Override
    public void save(SubscriptionAggregate aggregate) {

        LockedProductEntity productEntity =
                lockedProductRepository.findById(
                        aggregate.getLockedProductId().value()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "LockedProduct not found: " + aggregate.getLockedProductId()
                        )
                );

        SubscriptionEntity entity = SubscriptionMapper.toEntity(aggregate, productEntity);

        jpaRepository.save(entity);
    }
}
