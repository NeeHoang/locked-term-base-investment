package com.investment.lockedtermbasedinvestment.saving.application.service.impl;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.api.dto.request.CreateSubscriptionRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.ActivePackageResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.SubscriptionService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.LockedProductErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.LockedProductException;
import com.investment.lockedtermbasedinvestment.saving.domain.factory.EarningFactory;
import com.investment.lockedtermbasedinvestment.saving.domain.factory.SubscriptionFactory;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.EarningRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.LockedProductRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.SubscriptionRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.LockedProductId;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.WalletRef;
import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletErrorCode;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletException;
import com.investment.lockedtermbasedinvestment.wallet.domain.repository.WalletRepository;
import com.investment.lockedtermbasedinvestment.wallet.domain.valueobject.WalletId;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final WalletRepository walletRepository;
    private final LockedProductRepository lockedProductRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EarningRepository earningRepository;

    @Override
    @Transactional
    public SubscriptionAggregate create(String walletId, CreateSubscriptionRequest request) {

        // 1. load aggregate
        WalletAggregate wallet = walletRepository
                .findById(WalletId.from(walletId))
                .orElseThrow(() -> new WalletException(
                        WalletErrorCode.INVALID_WALLET_ID,
                        "Wallet not found with id: " + walletId
                ));

        LockedProductAggregate lockProduct = lockedProductRepository
                .findById(LockedProductId.from(request.productId()))
                .orElseThrow(() -> new LockedProductException(
                        LockedProductErrorCode.INVALID_PRODUCT_ID,
                        "Product not found with id: " + request.productId()
                ));

        // 2. validate domain logic
        Money principal = request.principal();
        wallet.lockSaving(principal);
        lockProduct.subscribe(principal);

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        // 3. Create subscription
        SubscriptionAggregate subscriptionAggregate = SubscriptionFactory.createNew(
                WalletRef.from(walletId),
                lockProduct.getId(),
                request.principal(),
                lockProduct.getInterestRate(),
                lockProduct.getTermDays(),
                LocalDate.now()
        );

        // 4. Create earning
        EarningAggregate earningAggregate = EarningFactory.createFromSubscription(
                subscriptionAggregate
        );

        // 5. persist
        walletRepository.update(wallet);
        lockedProductRepository.update(lockProduct);
        subscriptionRepository.save(subscriptionAggregate);
        earningRepository.save(earningAggregate);

        return subscriptionAggregate;
    }

    @Override
    public List<SubscriptionAggregate> getAllSubscribeToday() {
        return subscriptionRepository.findProductSubscribeToday();
    }

    @Override
    public List<SubscriptionAggregate> getSubscribeTodayById(String walletId) {

        WalletId id = WalletId.from(walletId);

        walletRepository.findById(id)
                .orElseThrow(() -> new WalletException(
                        WalletErrorCode.INVALID_WALLET_ID,
                        "Wallet not found with id: " + walletId
                ));

        LocalDate today = LocalDate.now();

        return subscriptionRepository.findSubscribeByWalletId(
                id.value(),
                today
        );
    }

    @Override
    public List<SubscriptionAggregate> getHistoryByWalletId(String id) {
        WalletId walletId = WalletId.from(id);
        return subscriptionRepository.findHistorySubscribe(walletId.value());
    }

    @Override
    public List<ActivePackageResponse> getActiveByWalletId(String id) {
        WalletId walletId = WalletId.from(id);
        return subscriptionRepository.findActiveSubscribe(walletId.value());
    }
}
