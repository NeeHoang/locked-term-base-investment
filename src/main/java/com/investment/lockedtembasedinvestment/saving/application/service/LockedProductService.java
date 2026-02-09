package com.investment.lockedtembasedinvestment.saving.application.service;

import com.investment.lockedtembasedinvestment.saving.api.dto.request.LockedProductRequest;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.LockedProductAggregate;

public interface LockedProductService {

    LockedProductAggregate create(LockedProductRequest request);
}
