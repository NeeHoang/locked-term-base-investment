package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.saving.api.dto.request.LockedProductRequest;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;

import java.util.List;

public interface LockedProductService {

    LockedProductAggregate create(LockedProductRequest request);

    LockedProductAggregate findById(Long id);

    List<LockedProductAggregate> findAll();

    List<LockedProductAggregate> findAllActive();

    List<LockedProductAggregate> findAllOperating();
}
