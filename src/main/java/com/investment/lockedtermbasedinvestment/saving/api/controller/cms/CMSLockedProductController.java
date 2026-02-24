package com.investment.lockedtermbasedinvestment.saving.api.controller.cms;

import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import com.investment.lockedtermbasedinvestment.saving.api.dto.request.LockedProductRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.LockedProductResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.LockedProductService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cms/locked-products")
@RequiredArgsConstructor
@Tag(
        name = "CMS - Locked Products",
        description = "CMS APIs for managing locked-term saving products"
)
public class CMSLockedProductController {

    private final LockedProductService service;

    @Operation(
            summary = "Get locked product by id",
            description = "Retrieve detailed information of a locked-term saving product by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locked product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Locked product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LockedProductResponse>> getProductById(
            @Parameter(description = "Locked product ID", example = "1")
            @PathVariable Long id)
    {
        LockedProductAggregate aggregate = service.findById(id);

        return ResponseEntity.ok(
                ApiResponseDTO.ok(
                        "Get locked product successfully",
                        toResponse(aggregate)
                )
        );
    }

    @Operation(
            summary = "Get all locked products",
            description = "Retrieve all locked-term saving products for CMS management"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of locked products retrieved successfully"
    )
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LockedProductResponse>>> getAllProduct() {

        List<LockedProductResponse> responses = service.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Get all product successfully",
                responses
                )
        );
    }

    @Operation(
            summary = "Create locked product",
            description = """
            Create a new locked-term saving product.
            This product will be available for user subscriptions after creation.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Locked product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "409", description = "Product conflict or duplicated configuration")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LockedProductResponse>> createLockedProduct(
             @Valid @RequestBody LockedProductRequest request
            ) {
        LockedProductAggregate aggregate = service.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created(
                        "Created locked product successfully",
                        toResponse(aggregate)
                )
                );
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
