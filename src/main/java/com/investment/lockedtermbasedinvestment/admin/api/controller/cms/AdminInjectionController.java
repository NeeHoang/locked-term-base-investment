package com.investment.lockedtermbasedinvestment.admin.api.controller.cms;

import com.investment.lockedtermbasedinvestment.admin.application.service.AdminInjectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cms/admin-injection")
public class AdminInjectionController {

    private final AdminInjectionService service;


}
