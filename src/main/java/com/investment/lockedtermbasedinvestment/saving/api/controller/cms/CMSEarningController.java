package com.investment.lockedtermbasedinvestment.saving.api.controller.cms;

import com.investment.lockedtermbasedinvestment.common.ApiResponseDTO;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.SumInterestPerDay;
import com.investment.lockedtermbasedinvestment.saving.application.service.EarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cms/earnings")
public class CMSEarningController {

    private final EarningService earningService;

    @GetMapping("/ets-daily-interest")
    public ResponseEntity<ApiResponseDTO<SumInterestPerDay>> getEtsDailyInterest() {

        return ResponseEntity.ok(ApiResponseDTO.ok(
                "Get ets daily interest successfully",
                earningService.getEtsDailyInterest()
        ));
    }
}
