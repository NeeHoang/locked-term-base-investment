package com.investment.lockedtermbasedinvestment.saving.api.controller.user;

import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import com.investment.lockedtermbasedinvestment.saving.api.dto.request.CreateSubscriptionRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.SubscriptionResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.SubscriptionService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/subscriptions")
@Slf4j
@RequiredArgsConstructor
@Tag(
        name = "User - Subscriptions",
        description = "User APIs for creating and viewing locked-term subscriptions"
)
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // U-01 -- AC-01-02-03
    @Operation(
            summary = "Create subscription",
            description = """
            Create a new locked-term subscription for the user wallet.
            The principal amount will be locked until maturity date.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "409", description = "Insufficient quota or duplicated subscription")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<SubscriptionResponse>> createSubscription(
            @Parameter(
                    description = "User wallet identifier",
                    required = true,
                    example = "3ff732cb-cf34-41fe-b587-3d2e8cb93c68"
            )
            @RequestHeader("X-WALLET-ID") String walletId,
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {

        SubscriptionAggregate aggregate = subscriptionService.create(
                walletId,
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created(
                        "Created subscription successfully",
                        toResponse(aggregate)));
    }

    // U-02 -- AC-03
    @Operation(
            summary = "Get subscriptions created today",
            description = "Retrieve subscriptions that were created today (system date, GMT+7)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of subscriptions created today"
    )
    @GetMapping("/registered-today")
    public ResponseEntity<ApiResponseDTO<List<SubscriptionResponse>>> getSubscribeToday(
            @Parameter(
                    description = "User wallet identifier",
                    required = true,
                    example = "3ff732cb-cf34-41fe-b587-3d2e8cb93c68"
            )
            @RequestHeader("X-WALLET-ID") String walletId
    ) {

        List<SubscriptionResponse> responses = subscriptionService
                .getSubscribeTodayById(walletId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Get subscription registered to day successfully",
                responses));
    }

    // U-02 -- AC-05
    @Operation(
            summary = "Get subscription history",
            description = "Retrieve all subscriptions associated with the given wallet"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subscription history retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid wallet id")
    })
    @GetMapping("/history")
    public ResponseEntity<ApiResponseDTO<List<SubscriptionResponse>>> getHistorySubscribe(
            @Parameter(
                    description = "User wallet identifier",
                    required = true,
                    example = "3ff732cb-cf34-41fe-b587-3d2e8cb93c68"
            )
            @RequestHeader("X-WALLET-ID") String walletId
    ) {

        List<SubscriptionResponse> responses = subscriptionService
                .getByWalletId(walletId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Get all history subscription successfully",
                responses));
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
