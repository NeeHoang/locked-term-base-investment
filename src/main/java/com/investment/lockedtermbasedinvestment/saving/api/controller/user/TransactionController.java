package com.investment.lockedtermbasedinvestment.saving.api.controller.user;

import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.EarningTransactionResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.InterestTransactionResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.WithdrawTransactionResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/withdraw")
    public ResponseEntity<ApiResponseDTO<List<WithdrawTransactionResponse>>> getWithdraw(
            @RequestHeader("X-WALLET-ID") String walletId
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.ok(
                        "Get withdraw transaction successfully",
                        transactionService.getWithdrawTx(walletId)
                )
        );
    }

    @GetMapping("/interest")
    public ResponseEntity<ApiResponseDTO<List<InterestTransactionResponse>>> getInterest(
            @RequestHeader("X-WALLET-ID") String walletId
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.ok(
                        "Get interest transaction successfully",
                        transactionService.getInterestTx(walletId)
                )
        );
    }

    @GetMapping("/earning")
    public ResponseEntity<ApiResponseDTO<List<EarningTransactionResponse>>> getEarning(
            @RequestHeader("X-WALLET-ID") String walletId
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.ok(
                        "Get earning transaction successfully",
                        transactionService.getEarningTx(walletId)
                )
        );
    }
}
