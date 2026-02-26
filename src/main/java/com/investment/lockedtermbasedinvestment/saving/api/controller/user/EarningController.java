package com.investment.lockedtermbasedinvestment.saving.api.controller.user;

import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import com.investment.lockedtermbasedinvestment.saving.api.dto.request.WithdrawRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.WithdrawResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.EarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/earnings")
@RequiredArgsConstructor
public class EarningController {

    private final EarningService earningService;

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponseDTO<WithdrawResponse>> withdraw(
            @RequestBody WithdrawRequest request,
            @RequestHeader("X-WALLET-ID") String walletId
            ) {

        return ResponseEntity.ok(
                ApiResponseDTO.ok(
                        "Withdraw successfully",
                        earningService.withdraw(request, walletId)
                ));
    }

    @PostMapping("{earningId}/early-redeem")
    public ResponseEntity<ApiResponseDTO<Void>> earlyRedeem(
            @PathVariable Long earningId,
            @RequestHeader("X-WALLET-ID") String walletId
    ) {
        earningService.earlyRedeem(earningId, walletId);

        return ResponseEntity.ok(
                ApiResponseDTO.ok(
                        "Early redeem processed successfully",
                        null)
        );
    }

}
