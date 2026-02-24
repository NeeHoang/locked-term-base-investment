package com.investment.lockedtermbasedinvestment.saving.api.controller.user;

import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.LockedProductResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.LockedProductService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user/locked-products")
@RequiredArgsConstructor
@Tag(
        name = "User - Locked Products",
        description = "User APIs for viewing available locked-term saving products"
)
public class LockedProductController {

    private final LockedProductService service;

    // U-02 -- AC-01
    @Operation(
            summary = "Get active locked products",
            description = """
            Retrieve all active locked-term saving products.
            Active products are available for new subscriptions.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of active locked products"
    )
    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO<List<LockedProductResponse>>> getActiveProducts() {

        List<LockedProductResponse> responses = service.findAllActive()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.ok(responses));
    }

    // U-02 -- AC-04
    @Operation(
            summary = "Get operating locked products",
            description = """
            Retrieve locked products that are currently operating.
            Operating products may still accrue interest but may not accept new subscriptions.
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of operating locked products"
    )
    @GetMapping("/operating")
    public ResponseEntity<ApiResponseDTO<List<LockedProductResponse>>> getOperatingProducts() {

        List<LockedProductResponse> responses = service.findAllOperating()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.ok(responses));
    }

    private LockedProductResponse toResponse(LockedProductAggregate aggregate) {
        return new LockedProductResponse(
                aggregate.getId().value(),
                aggregate.getTermDays().value(),
                aggregate.getInterestRate().value(),
                aggregate.getMinAmount().amount(),
                aggregate.getMaxAmount().amount(),
                aggregate.getTotalQuota().amount(),
                aggregate.getAvailableQuota().amount(),
                aggregate.getStatus().name()
        );
    }
}
