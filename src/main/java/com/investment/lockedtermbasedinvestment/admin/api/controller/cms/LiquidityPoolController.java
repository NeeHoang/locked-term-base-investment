package com.investment.lockedtermbasedinvestment.admin.api.controller.cms;

import com.investment.lockedtermbasedinvestment.admin.api.dto.request.LiquidityPoolRequest;
import com.investment.lockedtermbasedinvestment.admin.application.service.LiquidityPoolService;
import com.investment.lockedtermbasedinvestment.admin.domain.aggregate.LiquidityPoolAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/cms/liquidity-pool")
public class LiquidityPoolController {

    private final LiquidityPoolService liquidityPoolService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<LiquidityPoolAggregate> aggregates = liquidityPoolService.getAll();
        return ResponseEntity.ok(aggregates);
    }

    @PostMapping
    public void create(@RequestBody LiquidityPoolRequest request) {
        liquidityPoolService.createPool(request);
        log.info("Create pool successfully");
    }
}
