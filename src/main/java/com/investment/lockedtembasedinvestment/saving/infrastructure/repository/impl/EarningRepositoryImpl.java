package com.investment.lockedtembasedinvestment.saving.infrastructure.repository.impl;

import com.investment.lockedtembasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.repository.EarningRepository;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.EarningId;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.SubscriptionId;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.EarningEntity;
import com.investment.lockedtembasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;
import com.investment.lockedtembasedinvestment.saving.infrastructure.repository.JpaEarningRepository;
import com.investment.lockedtembasedinvestment.saving.infrastructure.repository.JpaSubscriptionRepository;
import com.investment.lockedtembasedinvestment.saving.mapper.EarningMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EarningRepositoryImpl implements EarningRepository {

    private final JpaEarningRepository jpaEarningRepository;
    private final JpaSubscriptionRepository jpaSubscriptionRepository;

    @Override
    public Optional<EarningAggregate> findById(EarningId id) {
        return jpaEarningRepository.findById(id.value())
                .map(EarningMapper::toDomain);
    }

    @Override
    public Optional<EarningAggregate> findBySubscriptionId(SubscriptionId subscriptionId) {
        return Optional.empty();
    }

    @Override
    public void save(EarningAggregate earning) {

        SubscriptionEntity subscriptionEntity = jpaSubscriptionRepository.findById(
                earning.getSubscriptionId().value()
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Subscription not found: " + earning.getSubscriptionId()
                )
        );

        EarningEntity entity = EarningMapper.toEntity(earning, subscriptionEntity);
        jpaEarningRepository.save(entity);
    }

    @Override
    public List<EarningAggregate> findAllActiveForAccrual(LocalDate today) {
        return List.of();
    }
}
