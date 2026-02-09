package com.investment.lockedtembasedinvestment.wallet.application.service.impl;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.wallet.api.dto.request.UserWalletRequest;
import com.investment.lockedtembasedinvestment.wallet.application.service.WalletService;
import com.investment.lockedtembasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtembasedinvestment.wallet.domain.factory.WalletFactory;
import com.investment.lockedtembasedinvestment.wallet.domain.repository.WalletRepository;
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
}
