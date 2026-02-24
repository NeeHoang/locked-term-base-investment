package com.investment.lockedtermbasedinvestment.wallet.api.controller.cms;

import com.investment.lockedtermbasedinvestment.wallet.application.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cms/user-wallets")
@Slf4j
@RequiredArgsConstructor
@Tag(
        name = "CMS - User Wallet",
        description = "CMS APIs for managing user wallets"
)
public class CMSUserWalletController {

    private final WalletService service;

    @Operation(
            summary = "Delete user wallet",
            description = """
            Permanently delete a user wallet.
            This operation is irreversible and should only be used by CMS/admin users.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Wallet deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Wallet not found"),
            @ApiResponse(responseCode = "409", description = "Wallet cannot be deleted due to active subscriptions")
    })
    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteUserWallet(
            @Parameter(
                    description = "Wallet identifier",
                    example = "3ff732cb-cf34-41fe-b587-3d2e8cb93c68",
                    required = true
            )
            @PathVariable String walletId) {

        service.deleteWallet(walletId);

        log.info("Delete successfully with walletId: {}", walletId);
        return ResponseEntity.noContent().build();
    }
}
