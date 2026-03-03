package com.investment.lockedtermbasedinvestment.saving.application.cron;

import com.investment.lockedtermbasedinvestment.admin.domain.exception.LiquidityPoolException;
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

    public BatchProcessResponse accrueDailyInterestOrMature(LocalDate today) {

        List<SubscriptionAggregate> allActive =
                subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);

        int total = allActive.size();
        int processed = 0;
        int skipped = 0;
        int ignored = 0;

        List<SkippedItemResponse> skippedItems = new ArrayList<>();

        for (SubscriptionAggregate subscription : allActive) {

            if (!subscription.isEligibleForDailyAccrual(today)) {
                ignored++;
                continue;
            }

            boolean interestOk = processInterest(subscription, today, skippedItems);
            processMature(subscription, today, skippedItems);

            if (interestOk) {
                processed++;
            } else {
                skipped++;
            }
        }
        BatchStatus status = resolveBatchStatus(total, processed, skipped, ignored);

        return new BatchProcessResponse(
                status,
                today,
                new BatchProcessResponse.Summary(
                        total, processed, skipped, ignored
                ),
                List.copyOf(skippedItems)
        );
    }

    private boolean processInterest(SubscriptionAggregate subscription,
                                    LocalDate today,
                                    List<SkippedItemResponse> skippedItems) {
        try {
            accrualProcessor.accrueInterestForSubscription(subscription, today);
            return true;

        } catch (DuplicateDailyInterestException exception) {
            skippedItems.add(new SkippedItemResponse(
                    subscription.getId().value().toString(),
                    exception.getEarningId(),
                    "DUPLICATE_DAILY_INTEREST"
            ));

            log.info("Skip duplicate interest. subscriptionId={}, earningId={}",
                    subscription.getId().value(), exception.getEarningId());
            return false;

        } catch (LiquidityPoolException ex) {
            skippedItems.add(new SkippedItemResponse(
                    subscription.getId().value().toString(),
                    null,
                    "POOL_INSUFFICIENT_INTEREST"
            ));

            log.warn("[POOL_INSUFFICIENT] Daily interest PENDING. subscriptionId={}, date={}",
                    subscription.getId(), today);
            return false;

        } catch (Exception ex) {
            skippedItems.add(new SkippedItemResponse(
                    subscription.getId().value().toString(),
                    null,
                    "UNEXPECTED_ERROR"
            ));
            log.error("[SYSTEM_ERROR] Daily interest FAILED. subscriptionId={}, date={}",
                    subscription.getId(), today, ex);
            return false;
        }
    }

    private void processMature(SubscriptionAggregate subscription,
                               LocalDate today,
                               List<SkippedItemResponse> skippedItems) {
        try {
            SubscriptionAggregate fresh = subscriptionRepository
                    .findById(subscription.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Subscription not found: " + subscription.getId().value()
                    ));
            accrualProcessor.matureSubscriptionIfEligible(fresh, today);

        } catch (LiquidityPoolException ex) {
            skippedItems.add(new SkippedItemResponse(
                    subscription.getId().value().toString(),
                    null,
                    "POOL_INSUFFICIENT_MATURE"
            ));
            log.warn("[POOL_INSUFFICIENT] Maturity PENDING. subscriptionId={}",
                    subscription.getId());

        } catch (Exception ex) {
            skippedItems.add(new SkippedItemResponse(
                    subscription.getId().value().toString(),
                    null,
                    "MATURE_ERROR"
            ));
            log.error("[SYSTEM_ERROR] Maturity FAILED. subscriptionId={}",
                    subscription.getId(), ex);
        }
    }

    private BatchStatus resolveBatchStatus(int total, int processed, int skipped, int ignored) {
        int actionable = total - ignored;
        if (actionable == 0)       return BatchStatus.SUCCESS;
        if (skipped == 0)          return BatchStatus.SUCCESS;
        if (processed == 0)        return BatchStatus.FAILED;
        return BatchStatus.PARTIAL_SUCCESS;
    }
}