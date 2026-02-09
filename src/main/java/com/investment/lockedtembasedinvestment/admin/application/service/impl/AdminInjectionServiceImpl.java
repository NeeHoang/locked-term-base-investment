package com.investment.lockedtembasedinvestment.admin.application.service.impl;

import com.investment.lockedtembasedinvestment.admin.api.dto.request.AdminInjectionRequest;
import com.investment.lockedtembasedinvestment.admin.api.dto.request.InjectionRequest;
import com.investment.lockedtembasedinvestment.admin.api.dto.response.AdminInjectionResponse;
import com.investment.lockedtembasedinvestment.admin.application.service.AdminInjectionService;
import com.investment.lockedtembasedinvestment.admin.infrastructure.repository.JpaAdminInjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminInjectionServiceImpl implements AdminInjectionService {

    private final JpaAdminInjectionRepository repository;

    @Override
    public AdminInjectionResponse create(AdminInjectionRequest request) {
        return null;
    }

    @Override
    public void inject(InjectionRequest request) {

    }
}
