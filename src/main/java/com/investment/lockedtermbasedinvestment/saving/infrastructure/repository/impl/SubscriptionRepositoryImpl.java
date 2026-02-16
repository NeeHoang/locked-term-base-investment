package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.impl;

import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.SubscriptionRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.SubscriptionId;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.LockedProductEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaLockedProductRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaSubscriptionRepository;
import com.investment.lockedtermbasedinvestment.saving.mapper.SubscriptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public List<SubscriptionAggregate> findProductSubscribeToday() {
        return jpaRepository.findByStartDate(LocalDate.now())
                .stream()
                .map(SubscriptionMapper::toDomain)
                .toList();
    }

    @Override
    public List<SubscriptionAggregate> findHistorySubscribe(UUID walletId) {
        return jpaRepository.findByWalletId(walletId)
                .stream()
                .map(SubscriptionMapper::toDomain)
                .toList();
    }
}
