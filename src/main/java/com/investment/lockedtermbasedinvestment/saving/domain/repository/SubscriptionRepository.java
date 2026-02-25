package com.investment.lockedtermbasedinvestment.saving.domain.repository;

import com.investment.lockedtermbasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.ActivePackageResponse;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.SubscriptionId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository {

    Optional<SubscriptionAggregate> findById(SubscriptionId id);

    void save(SubscriptionAggregate aggregate);

    List<SubscriptionAggregate> findProductSubscribeToday();

    List<SubscriptionAggregate> findSubscribeByWalletId(UUID walletId, LocalDate today);

    List<SubscriptionAggregate> findHistorySubscribe(UUID walletId);

    List<ActivePackageResponse> findActiveSubscribe(UUID walletId);

    List<SubscriptionAggregate> findByStatus(SubscriptionStatus status);
}
