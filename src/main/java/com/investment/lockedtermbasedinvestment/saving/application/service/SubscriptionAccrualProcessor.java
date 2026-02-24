package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtermbasedinvestment.admin.domain.repository.LiquidityPoolRepository;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityLedgerEntity;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaLiquidityLedgerRepository;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.DuplicateDailyInterestException;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.EarningRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.InterestTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaEarningTransactionRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaInterestTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionAccrualProcessor {

    private final EarningRepository earningRepository;
    private final LiquidityPoolRepository liquidityPoolRepository;
    private final JpaLiquidityLedgerRepository liquidityLedgerRepository;
    private final JpaEarningTransactionRepository earningTxRepository;
    private final JpaInterestTransactionRepository interestTxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void accrueForSubscription(
            SubscriptionAggregate subscription,
            LocalDate today
    ) {

        EarningAggregate earning = earningRepository
                .findBySubscriptionId(subscription.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Earning not found for subscription " + subscription.getId()
                ));

        Money earningBefore = earning.getAvailable();

        subscription.accrueDaily(earning, today);

        Money earningAfter = earning.getAvailable();

        if (!earningAfter.isGreaterThan(earningBefore)) {
            return;
        }

        Money interestAmount = earningAfter.subtract(earningBefore);
        Long earningId = earning.getId().value();

        recordInterestSafely(earningId, today, interestAmount);

        LiquidityPoolAggregate pool = loadSoloPool();

        Money poolBefore = pool.getTotalAmount();
        pool.debit(interestAmount);
        Money poolAfter = pool.getTotalAmount();

        liquidityLedgerRepository.save(
                LiquidityLedgerEntity.dailyInterestDebit(
                        poolBefore,
                        interestAmount,
                        poolAfter,
                        earningId.toString().getBytes()
                )
        );

        liquidityPoolRepository.save(pool);

        earningTxRepository.save(
                EarningTransactionEntity.dailyAccrual(
                        earningId,
                        earningBefore,
                        interestAmount,
                        earningAfter
                )
        );

        earningRepository.update(earning);
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