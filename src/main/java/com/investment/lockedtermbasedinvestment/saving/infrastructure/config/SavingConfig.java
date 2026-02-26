package com.investment.lockedtermbasedinvestment.saving.infrastructure.config;

import com.investment.lockedtermbasedinvestment.saving.domain.policy.PenaltyPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SavingConfig {

    @Bean
    public PenaltyPolicy penaltyPolicy() {
        return new PenaltyPolicy();
    }
}
