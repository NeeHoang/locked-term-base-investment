package com.investment.lockedtembasedinvestment.admin.application.service;

import com.investment.lockedtembasedinvestment.admin.api.dto.request.AdminInjectionRequest;
import com.investment.lockedtembasedinvestment.admin.api.dto.request.InjectionRequest;
import com.investment.lockedtembasedinvestment.admin.api.dto.response.AdminInjectionResponse;

public interface AdminInjectionService {

    AdminInjectionResponse create(AdminInjectionRequest request);
    void inject(InjectionRequest request);
}
