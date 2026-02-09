package com.investment.lockedtembasedinvestment.wallet.api.controller.user;

import com.investment.lockedtembasedinvestment.wallet.api.dto.request.UserWalletRequest;
import com.investment.lockedtembasedinvestment.wallet.api.dto.response.UserWalletResponse;
import com.investment.lockedtembasedinvestment.wallet.application.service.WalletService;
import com.investment.lockedtembasedinvestment.wallet.domain.aggregate.WalletAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/wallet")
@RequiredArgsConstructor
public class UserWalletController {

    private final WalletService service;

    @PostMapping
    public ResponseEntity<?> createWallet(
            @RequestBody UserWalletRequest request
    ) {

        WalletAggregate aggregate = service.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(aggregate));
    }

    private UserWalletResponse toResponse(WalletAggregate aggregate) {
        return new UserWalletResponse(
                aggregate.getId().value(),
                aggregate.getTotalBalance().amount(),
                aggregate.getBalanceAvailable().amount(),
                aggregate.getBalanceFrozen().amount(),
                aggregate.getStatus().name()
        );
    }
}
