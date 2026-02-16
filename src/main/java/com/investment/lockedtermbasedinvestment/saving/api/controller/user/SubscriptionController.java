package com.investment.lockedtermbasedinvestment.saving.api.controller.user;

import com.investment.lockedtermbasedinvestment.saving.api.dto.request.CreateSubscriptionRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.SubscriptionResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.SubscriptionService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/subscription")
@Slf4j
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // U-01 -- AC-01-02-03
    @PostMapping("/")
    public ResponseEntity<?> createSubscription(
            @RequestHeader("X-WALLET-ID") String walletId,
            @RequestBody CreateSubscriptionRequest request
            ) {

        SubscriptionAggregate aggregate = subscriptionService.create(
                walletId,
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(aggregate));
    }

    // U-02 -- AC-03
    @GetMapping("/registered-today")
    public ResponseEntity<List<SubscriptionResponse>> getSubscribeToday() {

        List<SubscriptionResponse> responses = subscriptionService
                .getSubscribeToday()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    // U-02 -- AC-05
    @GetMapping("/history")
    public ResponseEntity<List<SubscriptionResponse>> getHistorySubscribe(
            @RequestHeader("X-WALLET-ID") String walletId
    ) {

        List<SubscriptionResponse> responses = subscriptionService
                .getByWalletId(walletId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    private SubscriptionResponse toResponse(SubscriptionAggregate aggregate) {

        if (aggregate == null) {
            return null;
        }

        return new SubscriptionResponse(
                aggregate.getId().value(),
                aggregate.getWalletRef().value(),
                aggregate.getLockedProductId().value(),
                aggregate.getPrincipal().amount(),
                aggregate.getInterestRate().value(),
                aggregate.getTermDays().value(),
                aggregate.getTotalInterest().amount(),
                aggregate.getStartDate(),
                aggregate.getMaturityDate(),
                aggregate.getStatus().name()
        );
    }
}
