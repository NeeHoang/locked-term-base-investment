package com.investment.lockedtermbasedinvestment.admin.infrastructure.config;

import com.investment.lockedtermbasedinvestment.admin.domain.factory.LiquidityPoolFactory;
import com.investment.lockedtermbasedinvestment.admin.domain.policy.DefaultLiquidityPoolStatusPolicy;
import com.investment.lockedtermbasedinvestment.admin.domain.policy.LiquidityPoolStatusPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiquidityPoolConfig {

    @Bean
    public LiquidityPoolStatusPolicy liquidityPoolStatusPolicy() {
        return new DefaultLiquidityPoolStatusPolicy();
    }

    @Bean
    public LiquidityPoolFactory liquidityPoolFactory(LiquidityPoolStatusPolicy statusPolicy) {
        return new LiquidityPoolFactory(statusPolicy);
    }
}
