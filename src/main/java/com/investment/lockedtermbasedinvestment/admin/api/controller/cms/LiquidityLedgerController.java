package com.investment.lockedtermbasedinvestment.admin.api.controller.cms;

import com.investment.lockedtermbasedinvestment.admin.api.dto.response.LiquidityLedgerResponse;
import com.investment.lockedtermbasedinvestment.admin.application.service.LiquidityLedgerService;
import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cms/liquidity-ledger")
public class LiquidityLedgerController {

    private final LiquidityLedgerService liquidityLedgerService;

    @GetMapping("/ledger-tx")
    public ResponseEntity<ApiResponseDTO<List<LiquidityLedgerResponse>>> getLedgerTx() {

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Get ledger tx successfully",
                liquidityLedgerService.getAllTx()
        ));
    }
}
