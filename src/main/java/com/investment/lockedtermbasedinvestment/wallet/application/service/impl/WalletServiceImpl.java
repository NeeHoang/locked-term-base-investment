package com.investment.lockedtermbasedinvestment.wallet.application.service.impl;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.wallet.api.dto.request.UserWalletRequest;
import com.investment.lockedtermbasedinvestment.wallet.application.service.WalletService;
import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletErrorCode;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletException;
import com.investment.lockedtermbasedinvestment.wallet.domain.factory.WalletFactory;
import com.investment.lockedtermbasedinvestment.wallet.domain.repository.WalletRepository;
import com.investment.lockedtermbasedinvestment.wallet.domain.valueobject.WalletId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl  implements WalletService {

    private final WalletRepository repository;

    @Override
    public WalletAggregate save(UserWalletRequest request) {

        WalletAggregate aggregate = WalletFactory.createNew(
                Money.of(request.balanceAvailable())
        );

        repository.save(aggregate);

        return aggregate;
    }

    @Override
    public WalletAggregate getById(String id) {
        WalletId walletId = WalletId.from(id);

        return repository.findById(walletId)
                .orElseThrow(() -> new WalletException(
                        WalletErrorCode.WALLET_NOT_FOUND,
                        "Wallet not found with id: " + id
                ));
    }

    @Override
    public void deleteWallet(String id) {

        WalletId walletId = WalletId.from(id);

        WalletAggregate walletAggregate = repository
                .findById(walletId)
                        .orElseThrow(() -> new WalletException(
                                WalletErrorCode.WALLET_NOT_FOUND,
                                "Wallet not found with id: " + id
                        ));

        repository.delete(walletAggregate);
    }

}
