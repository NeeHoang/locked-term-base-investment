package com.investment.lockedtermbasedinvestment.admin.application.service;

import com.investment.lockedtermbasedinvestment.admin.api.dto.request.AdminInjectionRequest;
import com.investment.lockedtermbasedinvestment.admin.api.dto.request.InjectionRequest;
import com.investment.lockedtermbasedinvestment.admin.api.dto.response.AdminInjectionResponse;

public interface AdminInjectionService {

    AdminInjectionResponse inject(InjectionRequest request);
}
