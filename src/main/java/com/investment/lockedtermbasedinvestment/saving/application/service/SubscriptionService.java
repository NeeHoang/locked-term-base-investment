package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.saving.api.dto.request.CreateSubscriptionRequest;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;

import java.util.List;

public interface SubscriptionService {

    SubscriptionAggregate create(String walletId, CreateSubscriptionRequest request);

    List<SubscriptionAggregate> getAllSubscribeToday();

    List<SubscriptionAggregate> getSubscribeTodayById(String walletId);

    List<SubscriptionAggregate> getHistoryByWalletId(String walletId);

    List<SubscriptionAggregate> getActiveByWalletId(String walletId);
}
