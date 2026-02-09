package com.investment.lockedtembasedinvestment.saving.application.service.impl;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.api.dto.request.LockedProductRequest;
import com.investment.lockedtembasedinvestment.saving.application.service.LockedProductService;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.factory.LockedProductFactory;
import com.investment.lockedtembasedinvestment.saving.domain.repository.LockedProductRepository;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.InterestRate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.TermDays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LockedProductServiceImpl implements LockedProductService {

    private final LockedProductRepository repository;

    @Override
    @Transactional
    public LockedProductAggregate create(LockedProductRequest request) {

        if (request.termDays() == null) throw new IllegalArgumentException("termDays is required");

        TermDays termDays = new TermDays(request.termDays());
        InterestRate interestRate = new InterestRate(request.interestRate());
        Money minAmount = Money.of(request.minAmount());
        Money maxAmount = Money.of(request.maxAmount());
        Money totalQuota = Money.of(request.totalQuota());

        LockedProductAggregate aggregate =
                LockedProductFactory.createNew(
                        termDays,
                        interestRate,
                        minAmount,
                        maxAmount,
                        totalQuota,
                        request.description()
                );

        repository.save(aggregate);

        return aggregate;
    }
}
