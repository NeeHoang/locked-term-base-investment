package com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.impl;

import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.EarningRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.EarningId;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.SubscriptionId;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.SubscriptionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaEarningRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaSubscriptionRepository;
import com.investment.lockedtermbasedinvestment.saving.mapper.EarningMapper;
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
    public EarningAggregate save(EarningAggregate earning) {

        SubscriptionEntity subscriptionEntity =
                jpaSubscriptionRepository.findById(
                        earning.getSubscriptionId().value()
                ).orElseThrow(() ->
                        new IllegalArgumentException(
                                "Subscription not found: " + earning.getSubscriptionId()
                        )
                );

        EarningEntity entity = EarningMapper.toEntity(
                earning,
                subscriptionEntity
        );

        EarningEntity saved = jpaEarningRepository.save(entity);

        return EarningMapper.toDomain(saved);
    }

    @Override
    public Optional<EarningAggregate> findById(EarningId id) {
        return jpaEarningRepository
                .findById(id.value())
                .map(EarningMapper::toDomain);
    }

    @Override
    public Optional<EarningAggregate> findBySubscriptionId(SubscriptionId subscriptionId) {
        return jpaEarningRepository
                .findBySubscription_SubscriptionId(subscriptionId.value())
                .map(EarningMapper::toDomain);
    }

    @Override
    public void update(EarningAggregate earning) {

        EarningEntity entity = jpaEarningRepository.findById(
                earning.getId().value()
        ).orElseThrow(() ->
                new IllegalStateException(
                        "Earning not found: " + earning.getId()
                )
        );

        // Hibernate dirty checking auto update & increment version
        entity.setPrincipal(earning.getPrincipal().amount());
        entity.setTotalInterest(earning.getTotalInterest().amount());
        entity.setAvailable(earning.getAvailable().amount());
        entity.setHoldingDays(earning.getHoldingDays().value());
        entity.setInterestPerDay(earning.getInterestPerDay().amount());
        entity.setPenaltyAmount(earning.getPenaltyAmount().amount());
        entity.setPenaltyRate(earning.getPenaltyRate().value());
        entity.setTermDays(earning.getTermDays().value());
        entity.setProgress(earning.getProgress().value());
    }

    @Override
    public List<EarningAggregate> findAllActiveForAccrual(LocalDate today) {
        return List.of();
    }
}
