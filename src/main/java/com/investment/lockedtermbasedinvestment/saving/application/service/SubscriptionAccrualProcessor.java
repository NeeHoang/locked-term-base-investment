package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtermbasedinvestment.admin.domain.exception.LiquidityPoolErrorCode;
import com.investment.lockedtermbasedinvestment.admin.domain.exception.LiquidityPoolException;
import com.investment.lockedtermbasedinvestment.admin.domain.repository.LiquidityPoolRepository;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityLedgerEntity;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaLiquidityLedgerRepository;
import com.investment.lockedtermbasedinvestment.common.enums.EarningTxType;
import com.investment.lockedtermbasedinvestment.common.enums.LiquidityTransactionType;
import com.investment.lockedtermbasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.DuplicateDailyInterestException;
import com.investment.lockedtermbasedinvestment.saving.domain.policy.PenaltyPolicy;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.EarningRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.SubscriptionRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.InterestTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaEarningTransactionRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaInterestTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionAccrualProcessor {

    private final EarningRepository earningRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final LiquidityPoolRepository liquidityPoolRepository;
    private final JpaLiquidityLedgerRepository liquidityLedgerRepository;
    private final JpaEarningTransactionRepository earningTxRepository;
    private final JpaInterestTransactionRepository interestTxRepository;
    private final PenaltyPolicy penaltyPolicy;
    private final EarningTxStatusUpdater earningTxStatusUpdater;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void accrueForSubscription(
            SubscriptionAggregate subscription,
            LocalDate today
    ) {

        EarningAggregate earning = earningRepository
                .findBySubscriptionId(subscription.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Earning not found for subscription " + subscription.getId()
                ));

        // Daily interest
        if (
                subscription.isEligibleForDailyAccrual(today)
                && !today.isAfter(subscription.getMaturityDate())
        ) {
            processInterestStep(subscription, earning, today);
        }

        // Mature
        if (
                subscription.getStatus() == SubscriptionStatus.ACTIVE
                        && (today.isEqual(subscription.getMaturityDate())
                        || today.isAfter(subscription.getMaturityDate()))
        ) {
            processMaturityStep(subscription, earning);
        }

        // Update aggregate
        earningRepository.update(earning);
        subscriptionRepository.update(subscription);
    }

    private void processInterestStep(SubscriptionAggregate subscription,
                                     EarningAggregate earning,
                                     LocalDate today) {

        Money before = earning.getAvailable();

        // Create log PENDING and saveAndFlush
        EarningTransactionEntity tx = EarningTransactionEntity
                .createPending(
                        earning.getId().value(),
                        EarningTxType.DAILY_INTEREST,
                        before);
        earningTxRepository.saveAndFlush(tx);

        try {
            // Domain logic
            earning.accrueOneDay(penaltyPolicy);
            Money interestAmount = earning.getInterestPerDay();

            // Debit pool
            processLiquidityDebit(interestAmount,
                    tx.getTxId(),
                    LiquidityTransactionType.DAILY_INTEREST);

            // Pool success -> create InterestTx
            recordInterestSafely(earning.getId().value(), today, interestAmount);

            // Update EarningTransaction PENDING -> SUCCESS
            tx.markSuccess(interestAmount, earning.getAvailable());
            earningTxRepository.save(tx);

        } catch (LiquidityPoolException ex) {
            // Pool total amount < interest amount, earningTx Pending
            earningTxStatusUpdater.markPending(tx);

            log.warn("[POOL_INSUFFICIENT Daily interest PENDING. sub={}, date={}, required={}",
                    subscription.getId().value(), today, earning.getInterestPerDay()
            );

            throw new LiquidityPoolException(
                    LiquidityPoolErrorCode.INSUFFICIENT_BALANCE,
                    "Pool totalAmount not enough for debit daily interest, sub=" + subscription.getId().value()
            );

        } catch (Exception ex) {
            // System error -> FAILED
            earningTxStatusUpdater.markFailed(tx);

            log.error("[SYSTEM_ERROR] Daily interest FAILED. sub={}, date={}",
                    subscription.getId().value(), today, ex
            );

            throw ex; // Rollback earning aggregate (earningRepository.update not called)
        }
    }

    private void processMaturityStep(SubscriptionAggregate subscription,
                                     EarningAggregate earning) {
        Money before = earning.getAvailable();

        EarningTransactionEntity tx = EarningTransactionEntity
                .createPending(
                        earning.getId().value(),
                        EarningTxType.REDEEMED, before);
        earningTxRepository.saveAndFlush(tx);

        try {
            // Domain logic (Status ACTIVE -> MATURE)
            subscription.mature(earning);
            Money principal = subscription.getPrincipal();

            // Create record Liquidity Ledger
            processLiquidityDebit(principal, tx.getTxId(), LiquidityTransactionType.REDEEMED);

            // Update status EarningTransaction PENDING -> SUCCESS
            tx.markSuccess(principal, earning.getAvailable());
            earningTxRepository.save(tx);

        } catch (LiquidityPoolException ex) {
            earningTxStatusUpdater.markPending(tx);

            log.warn("[POOL_INSUFFICIENT] Maturity PENDING. sub={}",
                    subscription.getId().value());

            throw new LiquidityPoolException(
                    LiquidityPoolErrorCode.INSUFFICIENT_BALANCE,
                    "Pool totalAmount not enough for debit daily interest, sub=" + subscription.getId().value()
            );
        } catch (Exception ex) {
            earningTxStatusUpdater.markFailed(tx);

            log.error("[SYSTEM_ERROR] Maturity FAILED. sub={}", subscription.getId().value(), ex);

            throw ex;        }
    }

    private void processLiquidityDebit(Money amount,
                                       byte[] refId,
                                       LiquidityTransactionType type) {

        LiquidityPoolAggregate pool = loadSoloPool();
        Money poolBefore = pool.getTotalAmount();

        pool.debit(amount); // throws Liquidity pool exception if errors

        Money poolAfter = pool.getTotalAmount();

        LiquidityLedgerEntity ledger = switch (type) {

            case DAILY_INTEREST -> LiquidityLedgerEntity
                    .dailyInterestDebit(
                            poolBefore,
                            amount,
                            poolAfter,
                            refId
                    );

            case REDEEMED -> LiquidityLedgerEntity
                    .redemptionDebit(
                            poolBefore,
                            amount,
                            poolAfter,
                            refId
                    );

            default -> throw new IllegalArgumentException(
                    "Unsupported liquidity transaction type: " + type
            );
        };

        liquidityLedgerRepository.save(ledger);
        liquidityPoolRepository.save(pool);
    }

    private void recordInterestSafely(
            Long earningId,
            LocalDate date,
            Money interestAmount
    ) {
        try {
            interestTxRepository.saveAndFlush(
                    InterestTransactionEntity.dailyInterest(
                            earningId,
                            date,
                            interestAmount
                    )
            );
        } catch (DataIntegrityViolationException ex) {
            if (isDuplicateInterest(ex)) {
                throw new DuplicateDailyInterestException(earningId);
            }
            throw ex;
        }
    }

    private boolean isDuplicateInterest(DataIntegrityViolationException ex) {
        return ex.getMessage() != null
                && ex.getMessage().contains("uk_interest_tx_earning_date");
    }

    private LiquidityPoolAggregate loadSoloPool() {
        return liquidityPoolRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Liquidity pool not initialized"
                ));
    }
}