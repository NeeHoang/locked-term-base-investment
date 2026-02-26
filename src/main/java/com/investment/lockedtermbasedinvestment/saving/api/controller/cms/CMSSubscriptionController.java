package com.investment.lockedtermbasedinvestment.saving.api.controller.cms;

import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.BatchProcessResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.SubscriptionResponse;
import com.investment.lockedtermbasedinvestment.saving.application.cron.DailyInterestAccrualJob;
import com.investment.lockedtermbasedinvestment.saving.application.service.SubscriptionService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cms/subscriptions/jobs")
@Tag(
        name = "CMS - Subscription Jobs",
        description = "CMS endpoints for triggering subscription background jobs"
)
public class CMSSubscriptionController {

    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final DailyInterestAccrualJob accrualJob;

    private final SubscriptionService subscriptionService;


    // manual daily interest job
    @Operation(
            summary = "Trigger daily interest accrual job",
            description = """
            Manually trigger daily interest accrual job.
            This endpoint is used for CMS operations, testing scheduled jobs,
            or re-running interest calculation for a specific date.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Job executed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "409", description = "Job already executed for the given date")
    })
    @PostMapping("/daily-interest")
    public ResponseEntity<ApiResponseDTO<BatchProcessResponse>> runDailyAccrual(
            @Parameter(
                    description = "Execution date (yyyy-MM-dd). If not provided, defaults to today (GMT+7).",
                    example = "2026-02-23"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {

        LocalDate runDate = (date != null)
                ? date
                : LocalDate.now(VN_ZONE);

        BatchProcessResponse response = accrualJob.accrueDailyInterestOrMature(runDate);

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Daily interest manual cron job successfully",
                response
        ));
    }

    @Operation(
            summary = "Get subscriptions created today",
            description = "Retrieve subscriptions that were created today (system date, GMT+7)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of subscriptions created today"
    )
    @GetMapping("/registered-today")
    public ResponseEntity<ApiResponseDTO<List<SubscriptionResponse>>> getSubscribeToday() {

        List<SubscriptionResponse> responses = subscriptionService
                .getAllSubscribeToday()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Get all register subscription today successfully",
                responses
        ));
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
