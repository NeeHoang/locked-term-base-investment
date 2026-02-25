package com.investment.lockedtermbasedinvestment.admin.api.controller.cms;

import com.investment.lockedtermbasedinvestment.admin.api.dto.request.InjectionRequest;
import com.investment.lockedtermbasedinvestment.admin.api.dto.request.LiquidityPoolRequest;
import com.investment.lockedtermbasedinvestment.admin.api.dto.response.AdminInjectionResponse;
import com.investment.lockedtermbasedinvestment.admin.api.dto.response.LiquidityPoolResponse;
import com.investment.lockedtermbasedinvestment.admin.application.service.AdminInjectionService;
import com.investment.lockedtermbasedinvestment.admin.application.service.LiquidityPoolService;
import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/cms/liquidity-pool")
@Tag(
        name = "CMS - Liquidity Pool",
        description = "Manage liquidity pool and admin injections"
)
public class LiquidityPoolController {

    private final LiquidityPoolService liquidityPoolService;
    private final AdminInjectionService adminInjectionService;

    @Operation(
            summary = "Get all liquidity pool",
            description = "Retrieve all existing liquidity pool for CMS monitoring"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of liquidity pool retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "code": "SUCCESS",
                                        "message": "Retrieved liquidity pools successfully",
                                        "data": [
                                            {
                                                "id": " x019c6f62b1d6afcffcfb4323b3dc7c52",
                                                "totalAmount": 100000,
                                                "minThreshold": 50000,
                                                "status": "NORMAL"
                                            }
                                        ]
                                    }
                                    """
                    )
            )
    )
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LiquidityPoolResponse>>> getAll(

    ) {

        List<LiquidityPoolAggregate> aggregates = liquidityPoolService.getAll();

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Get all liquidity pool successfully",
                toResponses(aggregates)
                )
        );
    }

    @Operation(
            summary = "Create liquidity pool",
            description = "Create a new liquidity pool used for locked-term investment products"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(
                    responseCode = "201",
                    description = "Liquidity pool created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "CREATED",
                                                "message": "Created liquidity pools successfully",
                                                "data": [
                                                    {}
                                                ]
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> create(
            @Valid @RequestBody LiquidityPoolRequest request
    ) {

        liquidityPoolService.createPool(request);
        log.info("Liquidity pool created successfully");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created(
                        "Created liquidity pool successfully",
                        null
                        )
                );
    }

    @Operation(
            summary = "Inject liquidity into pool",
            description = """
            Admin operation to inject funds into a liquidity pool.
            This action increases available liquidity for subscriptions.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Liquidity injected successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid injection request"),
            @ApiResponse(responseCode = "404", description = "Liquidity pool not found"),
            @ApiResponse(responseCode = "409", description = "Injection ´œconflict or duplicated request")
    })
    @PostMapping("/inject")
    public ResponseEntity<ApiResponseDTO<AdminInjectionResponse>> inject(
            // hard code 01HXQZK7M9F0A8K3R5YJ2D6V4B
            @RequestHeader("X-ADMIN-ID") String adminId,

            @Valid @RequestBody InjectionRequest request
            ) {

        AdminInjectionResponse response = adminInjectionService.inject(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created(
                        "Injection liquidity pool successfully",
                        response
                )
                );
    }

    private static LiquidityPoolResponse toResponse(LiquidityPoolAggregate aggregate) {
        return new LiquidityPoolResponse(
                aggregate.getId().value(),
                aggregate.getTotalAmount().amount(),
                aggregate.getMinThreshold().amount(),
                aggregate.getStatus().name()
        );
    }

    private static List<LiquidityPoolResponse> toResponses(List<LiquidityPoolAggregate> aggregates) {
        return aggregates.stream()
                .map(LiquidityPoolController::toResponse)
                .toList();
    }
}
