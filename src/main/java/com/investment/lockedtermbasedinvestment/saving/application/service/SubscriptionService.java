package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.saving.api.dto.request.CreateSubscriptionRequest;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;

import java.util.List;

public interface SubscriptionService {

    SubscriptionAggregate create(String walletId, CreateSubscriptionRequest request);

    SubscriptionAggregate getById(Long subscriptionId);

    List<SubscriptionAggregate> getByWallet(String walletId);

    List<SubscriptionAggregate> getSubscribeToday();

    List<SubscriptionAggregate> getByWalletId(String walletId);
}
