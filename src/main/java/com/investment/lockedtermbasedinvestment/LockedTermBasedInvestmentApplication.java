package com.investment.lockedtermbasedinvestment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LockedTermBasedInvestmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(LockedTermBasedInvestmentApplication.class, args);
    }

}
