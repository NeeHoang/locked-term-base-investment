package com.investment.lockedtermbasedinvestment.saving.application.service.impl;

import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtermbasedinvestment.admin.domain.repository.LiquidityPoolRepository;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityLedgerEntity;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaLiquidityLedgerRepository;
import com.investment.lockedtermbasedinvestment.common.enums.EarningTxType;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.api.dto.request.WithdrawRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.WithdrawResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.EarningService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningException;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionException;
import com.investment.lockedtermbasedinvestment.saving.domain.policy.PenaltyPolicy;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.EarningRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.SubscriptionRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.EarningId;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.WalletRef;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.WithdrawTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaEarningTransactionRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaWithdrawTransactionRepository;
import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletErrorCode;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletException;
import com.investment.lockedtermbasedinvestment.wallet.domain.repository.WalletRepository;
import com.investment.lockedtermbasedinvestment.wallet.domain.valueobject.WalletId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EarningServiceImpl implements EarningService {

    private final EarningRepository earningRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final WalletRepository walletRepository;
    private final PenaltyPolicy penaltyPolicy;
    private final LiquidityPoolRepository liquidityPoolRepository;

    private final JpaWithdrawTransactionRepository jpaWithdrawTransactionRepository;
    private final JpaEarningTransactionRepository jpaEarningTransactionRepository;
    private final JpaLiquidityLedgerRepository jpaLiquidityLedgerRepository;

    @Override
    @Transactional
    public WithdrawResponse withdraw(WithdrawRequest request, String walletId) {

        EarningAggregate earning = earningRepository
                .findById(EarningId.from(request.earningId()))
                .orElseThrow(() -> new EarningException(
                        EarningErrorCode.INVALID_EARNING_ID,
                        "Earning not found with id: " + request.earningId()
                ));

        SubscriptionAggregate subscription = subscriptionRepository
                .findById(earning.getSubscriptionId())
                .orElseThrow(() -> new SubscriptionException(
                        SubscriptionErrorCode.INVALID_SUBSCRIPTION_ID,
                        "Subscription not found with id: " + earning.getSubscriptionId().value()
                ));

        if (!subscription.getWalletRef().value().equals(WalletRef.from(walletId).value())) {
            throw new WalletException(
                    WalletErrorCode.ACCESS_DENIED,
                    "This earning does not belong to the provided wallet"
            );
        }

        WalletAggregate wallet = walletRepository
                .findById(WalletId.from(walletId))
                .orElseThrow(() -> new WalletException(
                        WalletErrorCode.INVALID_WALLET_ID,
                        "Wallet not found with id: " + walletId
                ));

        Money amountToWithdraw = Money.of(request.amount());

        Money availableBefore = earning.getAvailable();

        earning.withdraw(amountToWithdraw);
        wallet.depositEarnings(amountToWithdraw);

        Money availableAfter = earning.getAvailable();

        jpaWithdrawTransactionRepository.save(
                WithdrawTransactionEntity.withdraw(
                        earning.getId().value(),
                        availableBefore.amount(),
                        amountToWithdraw.amount(),
                        availableAfter.amount()
                )
        );

        earningRepository.update(earning);
        walletRepository.update(wallet);

        return new WithdrawResponse(
                earning.getId().value(),
                earning.getAvailable().amount(),
                wallet.getId().value(),
                wallet.getBalanceAvailable().amount(),
                wallet.getTotalBalance().amount()
        );
    }

    @Override
    @Transactional
    public void earlyRedeem(Long earningId, String walletId) {

        EarningAggregate earning = earningRepository
                .findById(EarningId.from(earningId))
                .orElseThrow(() -> new EarningException(
                        EarningErrorCode.INVALID_EARNING_ID,
                        "Earning not found"));

        SubscriptionAggregate subscription = subscriptionRepository
                .findByIdAndActive(earning.getSubscriptionId())
                .orElseThrow(() -> new SubscriptionException(
                        SubscriptionErrorCode.INVALID_SUBSCRIPTION_ID,
                        "Subscription not found"));

        if (!subscription.getWalletRef().value().equals(WalletRef.from(walletId).value())) {
            throw new WalletException(
                    WalletErrorCode.ACCESS_DENIED,
                    "This earning does not belong to the provided wallet"
            );
        }

        WalletAggregate wallet = walletRepository
                .findById(WalletId.from(walletId))
                .orElseThrow(() -> new WalletException(
                        WalletErrorCode.INVALID_WALLET_ID,
                        "Wallet not found"));

        LiquidityPoolAggregate liquidityPool = liquidityPoolRepository.findSolo();

        Money principal = subscription.getPrincipal();
        Money availableBefore = earning.getAvailable();

        // create EarningTx (PENDING)
        EarningTransactionEntity tx =
                EarningTransactionEntity.createPending(
                        earningId,
                        EarningTxType.EARLY_REDEEMED,
                        availableBefore
                );
        jpaEarningTransactionRepository.saveAndFlush(tx);

        try {
            // Business domain logic

            // Calculator penalty rate and update status Active -> Early_REDEEM
            subscription.earlyRedeem(earning, penaltyPolicy);

            // Update total balance and frozen balance user wallet, principal -> available Earning
            wallet.releasePrincipalToEarning(principal);

            Money availableAfter = earning.getAvailable();
            // Calculator netAmountToDebitFromPool
            Money netAmountToDebitFromPool = availableAfter.subtract(availableBefore);

            // Debit pool Money
            Money poolBefore = liquidityPool.getTotalAmount();
            liquidityPool.debit(netAmountToDebitFromPool);

            LiquidityLedgerEntity ledgerTx =
                    LiquidityLedgerEntity.EarlyRedeemDebit(
                            poolBefore,
                            netAmountToDebitFromPool,
                            liquidityPool.getTotalAmount(),
                            tx.getTxId()
                    );
            jpaLiquidityLedgerRepository.save(ledgerTx);

            tx.markSuccess(netAmountToDebitFromPool, availableAfter);

            // Update DB
            subscriptionRepository.update(subscription);
            earningRepository.update(earning);
            walletRepository.update(wallet);
            liquidityPoolRepository.save(liquidityPool);
            jpaEarningTransactionRepository.save(tx);

        } catch (Exception ex) {
            log.error("Early redeem failed for earningId: {}", earningId, ex);
            tx.markFailed();
            jpaEarningTransactionRepository.save(tx);
            throw ex; // Rollback all
        }
    }
}
