package com.investment.lockedtermbasedinvestment.saving.application.cron;

import com.investment.lockedtermbasedinvestment.common.enums.BatchStatus;
import com.investment.lockedtermbasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.BatchProcessResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.SkippedItemResponse;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.DuplicateDailyInterestException;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.SubscriptionRepository;
import com.investment.lockedtermbasedinvestment.saving.application.service.SubscriptionAccrualProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyInterestAccrualJob {

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionAccrualProcessor accrualProcessor;

    public BatchProcessResponse accrueDailyInterest(LocalDate today) {

        List<SubscriptionAggregate> allActive =
                subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);

        int total = allActive.size();
        int processed = 0;
        int skipped = 0;
        int ignored = 0;

        List<SkippedItemResponse> duplicates = new ArrayList<>();

        for (SubscriptionAggregate subscription : allActive) {

            if (!subscription.isEligibleForDailyAccrual(today)) {
                ignored++;
                continue;
            }

            try {
                accrualProcessor.accrueForSubscription(subscription, today);
                processed++;

            } catch (DuplicateDailyInterestException ex) {
                skipped++;
                duplicates.add(new SkippedItemResponse(
                        subscription.getId().value().toString(),
                        ex.getEarningId(),
                        "DUPLICATE_DAILY_INTEREST"
                ));

                log.info(
                        "Skip duplicate interest. subscriptionId={}, earningId={}",
                        subscription.getId(),
                        ex.getEarningId()
                );
            } catch (Exception ex) {
                skipped++;
                duplicates.add(new SkippedItemResponse(
                        subscription.getId().value().toString(),
                        null,
                        "UNEXPECTED_ERROR"
                ));

                log.error(
                        "Daily interest failed. subscriptionId={}, data={}",
                        subscription.getId(),
                        today,
                        ex
                );
            }
        }

        BatchStatus status;
        if (processed == 0 && skipped == 0) {
            status = BatchStatus.SUCCESS;
        } else if (processed + skipped == total) {
            status = BatchStatus.SUCCESS;
        } else if (processed == 0) {
            status = BatchStatus.FAILED;
        } else {
            status = BatchStatus.PARTIAL_SUCCESS;
        }

        return new BatchProcessResponse(
                status,
                today,
                new BatchProcessResponse.Summary(
                        total,
                        processed,
                        skipped,
                        ignored
                ),
                List.copyOf(duplicates)
        );
    }
}

//package com.investment.lockedtermbasedinvestment.saving.application.cron;
//
//import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
//import com.investment.lockedtermbasedinvestment.admin.domain.repository.LiquidityPoolRepository;
//import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityLedgerEntity;
//import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaLiquidityLedgerRepository;
//import com.investment.lockedtermbasedinvestment.common.enums.BatchStatus;
//import com.investment.lockedtermbasedinvestment.common.enums.SubscriptionStatus;
//import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
//import com.investment.lockedtermbasedinvestment.saving.api.dto.response.BatchProcessResponse;
//import com.investment.lockedtermbasedinvestment.saving.api.dto.response.SkippedItemResponse;
//import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
//import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
//import com.investment.lockedtermbasedinvestment.saving.domain.exception.DuplicateDailyInterestException;
//import com.investment.lockedtermbasedinvestment.saving.domain.repository.EarningRepository;
//import com.investment.lockedtermbasedinvestment.saving.domain.repository.SubscriptionRepository;
//import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
//import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.InterestTransactionEntity;
//import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaEarningTransactionRepository;
//import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaInterestTransactionRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class DailyInterestAccrualJob {
//
//    private final SubscriptionRepository subscriptionRepository;
//    private final EarningRepository earningRepository;
//
//    private final LiquidityPoolRepository liquidityPoolRepository;
//    private final JpaLiquidityLedgerRepository liquidityLedgerRepository;
//
//    private final JpaEarningTransactionRepository earningTxRepository;
//    private final JpaInterestTransactionRepository interestTxRepository;
//
//    public BatchProcessResponse accrueDailyInterest(LocalDate today) {
//
//        List<SubscriptionAggregate> subscriptions =
//                subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);
//
//        int total = subscriptions.size();
//        int processed = 0;
//        int skipped = 0;
//
//        List<SkippedItemResponse> duplicates = new ArrayList<>();
//
//        for (SubscriptionAggregate subscription : subscriptions) {
//            try {
//                accrueForSubscription(subscription, today);
//                processed++;
//
//            } catch (DuplicateDailyInterestException ex) {
//                skipped++;
//                duplicates.add(new SkippedItemResponse(
//                        subscription.getId().value().toString(),
//                        ex.getEarningId(),
//                        "DUPLICATE_DAILY_INTEREST"
//                ));
//
//                log.info(
//                        "Skip duplicate interest. subscriptionId={}, earningId={}",
//                        subscription.getId(),
//                        ex.getEarningId()
//                );
//            } catch (Exception ex) {
//                skipped++;
//                duplicates.add(new SkippedItemResponse(
//                        subscription.getId().value().toString(),
//                        null,
//                        "UNEXPECTED_ERROR"
//                ));
//
//                log.error(
//                        "Daily interest failed. subscriptionId={}, data={}",
//                        subscription.getId(),
//                        today,
//                        ex
//                );
//            }
//        }
//
//        BatchStatus status;
//
//        if (total == 0) {
//            status = BatchStatus.SUCCESS;
//        } else if (processed == total) {
//            status = BatchStatus.SUCCESS;
//        } else if (processed == 0) {
//            status = BatchStatus.FAILED;
//        } else {
//            status = BatchStatus.PARTIAL_SUCCESS;
//        }
//
//        return new BatchProcessResponse(
//                status,
//                today,
//                new BatchProcessResponse.Summary(
//                        total,
//                        processed,
//                        skipped
//                ),
//                List.copyOf(duplicates)
//        );
//    }
//
//    @Transactional
//    private void accrueForSubscription(
//            SubscriptionAggregate subscription,
//            LocalDate today
//    ) {
//
//        EarningAggregate earning = earningRepository
//                .findBySubscriptionId(subscription.getId())
//                .orElseThrow(() -> new IllegalStateException(
//                        "Earning not found for subscription " + subscription.getId()
//                ));
//
//        Money earningBefore = earning.getAvailable();
//
//        subscription.accrueDaily(earning, today);
//
//        Money earningAfter = earning.getAvailable();
//
//        if (!earningAfter.isGreaterThan(earningBefore)) {
//            return;
//        }
//
//        Money interestAmount = earningAfter.subtract(earningBefore);
//        Long earningId = earning.getId().value();
//
//        recordInterestSafely(earningId, today, interestAmount);
//
//        LiquidityPoolAggregate pool = loadSoloPool();
//
//        Money poolBefore = pool.getTotalAmount();
//        pool.debit(interestAmount);
//        Money poolAfter = pool.getTotalAmount();
//
//        liquidityLedgerRepository.save(
//                LiquidityLedgerEntity.dailyInterestDebit(
//                        poolBefore,
//                        interestAmount,
//                        poolAfter,
//                        earningId.toString().getBytes()
//                )
//        );
//
//        liquidityPoolRepository.save(pool);
//
//        earningTxRepository.save(
//                EarningTransactionEntity.dailyAccrual(
//                        earningId,
//                        earningBefore,
//                        interestAmount,
//                        earningAfter
//                )
//        );
//
//        earningRepository.update(earning);
//    }
//
//    private void recordInterestSafely(
//            Long earningId,
//            LocalDate date,
//            Money interestAmount
//    ) {
//        try {
//            interestTxRepository.save(
//                    InterestTransactionEntity.dailyInterest(
//                            earningId,
//                            date,
//                            interestAmount
//                    )
//            );
//        } catch (DataIntegrityViolationException ex) {
//            if (isDuplicateInterest(ex)) {
//                throw new DuplicateDailyInterestException(earningId);
//            }
//            throw ex;
//        }
//    }
//
//    private boolean isDuplicateInterest(DataIntegrityViolationException ex) {
//        return ex.getMessage() != null
//                && ex.getMessage().contains("uk_interest_tx_earning_date");
//    }
//
//    private LiquidityPoolAggregate loadSoloPool() {
//        return liquidityPoolRepository.findAll()
//                .stream()
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException(
//                        "Liquidity pool not initialized"
//                ));
//    }
//}