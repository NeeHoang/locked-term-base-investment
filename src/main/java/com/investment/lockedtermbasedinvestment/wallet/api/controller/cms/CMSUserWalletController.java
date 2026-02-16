package com.investment.lockedtermbasedinvestment.wallet.api.controller.cms;

import com.investment.lockedtermbasedinvestment.wallet.application.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cms/user-wallet")
@Slf4j
@RequiredArgsConstructor
public class CMSUserWalletController {

    private final WalletService service;

    @DeleteMapping("/{walletId}")
    public ResponseEntity<?> deleteUserWallet(@PathVariable String walletId) {

        service.deleteWallet(walletId);

        log.info("Delete successfully with walletId: {}", walletId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Delete successfully with walletId: " + walletId);
    }
}
