package com.investment.lockedtermbasedinvestment.wallet.api.controller.user;

import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import com.investment.lockedtermbasedinvestment.wallet.api.dto.request.UserWalletRequest;
import com.investment.lockedtermbasedinvestment.wallet.api.dto.response.UserWalletResponse;
import com.investment.lockedtermbasedinvestment.wallet.application.service.WalletService;
import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/wallet")
@RequiredArgsConstructor
@Tag(
        name = "User Wallet",
        description = "APIs for user wallet operations"
)
public class UserWalletController {

    private final WalletService service;

    @Operation(
            summary = "Get current user's wallet",
            description = "Retrieve wallet information using wallet ID from request header"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Wallet retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Wallet not found")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<UserWalletResponse>> getWallet(
            @Parameter(
                    name = "X-WALLET-ID",
                    description = "Wallet ID of current user",
                    required = true,
                    example = "3ff732cb-cf34-41fe-b587-3d2e8cb93c68",
                    in = ParameterIn.HEADER
            )
            @RequestHeader("X-WALLET-ID") String walletId
    ) {
        WalletAggregate aggregate = service.getById(walletId);

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Get wallet info successfully",
                toResponse(aggregate)
        ));
    }

    @Operation(
            summary = "Create wallet",
            description = "Create a new wallet for user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Wallet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserWalletResponse>> createWallet(
            @RequestBody UserWalletRequest request
    ) {

        WalletAggregate aggregate = service.save(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.created(
                        "Created user wallet successfully",
                        toResponse(aggregate)));
    }

    private UserWalletResponse toResponse(WalletAggregate aggregate) {
        return new UserWalletResponse(
                aggregate.getId().value(),
                aggregate.getTotalBalance().amount(),
                aggregate.getBalanceAvailable().amount(),
                aggregate.getBalanceFrozen().amount(),
                aggregate.getStatus().name()
        );
    }
}
