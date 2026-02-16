package com.investment.lockedtermbasedinvestment.wallet.api.controller.user;

import com.investment.lockedtermbasedinvestment.wallet.api.dto.request.UserWalletRequest;
import com.investment.lockedtermbasedinvestment.wallet.api.dto.response.UserWalletResponse;
import com.investment.lockedtermbasedinvestment.wallet.application.service.WalletService;
import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/wallet")
@RequiredArgsConstructor
public class UserWalletController {

    private final WalletService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable("id") String id) {
        WalletAggregate aggregate = service.getById(id);
        return ResponseEntity.ok(toResponse(aggregate));
    }


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
