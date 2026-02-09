package com.investment.lockedtembasedinvestment.admin.api.controller.cms;

import com.investment.lockedtembasedinvestment.admin.api.dto.request.LiquidityPoolRequest;
import com.investment.lockedtembasedinvestment.admin.application.service.LiquidityPoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/cms/liquidity-pool")
public class LiquidityPoolController {

    private final LiquidityPoolService liquidityPoolService;

    @PostMapping
    public void create(@RequestBody LiquidityPoolRequest request) {
        liquidityPoolService.createPool(request);
        log.info("Create pool successfully");
    }
}
