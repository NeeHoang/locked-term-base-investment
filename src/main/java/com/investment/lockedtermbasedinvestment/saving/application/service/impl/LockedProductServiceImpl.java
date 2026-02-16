package com.investment.lockedtermbasedinvestment.saving.application.service.impl;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.api.dto.request.LockedProductRequest;
import com.investment.lockedtermbasedinvestment.saving.application.service.LockedProductService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.LockedProductErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.LockedProductException;
import com.investment.lockedtermbasedinvestment.saving.domain.factory.LockedProductFactory;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.LockedProductRepository;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.InterestRate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.LockedProductId;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.TermDays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Override
    public LockedProductAggregate findById(Long id) {

        return repository.findById(LockedProductId.from(id))
                .orElseThrow(() -> new LockedProductException(
                        LockedProductErrorCode.INVALID_PRODUCT_ID,
                        "Product not found with id: " + id
                ));
    }

    @Override
    public List<LockedProductAggregate> findAll() {
        return repository.findAll();
    }

    @Override
    public List<LockedProductAggregate> findAllActive() {
        return repository.findAllActive();
    }

    @Override
    public List<LockedProductAggregate> findAllOperating() {
        return repository.findAllOperating();
    }
}
