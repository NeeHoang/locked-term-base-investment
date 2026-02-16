package com.investment.lockedtermbasedinvestment.saving.api.controller.user;

import com.investment.lockedtermbasedinvestment.saving.api.dto.response.LockedProductResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.LockedProductService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user/locked-products")
@RequiredArgsConstructor
public class LockedProductController {

    private final LockedProductService service;

    // U-02 -- AC-01
    @GetMapping("/active")
    public ResponseEntity<List<LockedProductResponse>> getActiveProducts() {
        List<LockedProductResponse> responses = service.findAllActive()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    // U-02 -- AC-04
    @GetMapping("/operating")
    public ResponseEntity<List<LockedProductResponse>> getOperatingProducts() {
        List<LockedProductResponse> responses = service.findAllOperating()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    private LockedProductResponse toResponse(LockedProductAggregate aggregate) {
        return new LockedProductResponse(
                aggregate.getId().value(),
                aggregate.getTermDays().value(),
                aggregate.getInterestRate().value(),
                aggregate.getMinAmount().amount(),
                aggregate.getMaxAmount().amount(),
                aggregate.getTotalQuota().amount(),
                aggregate.getAvailableQuota().amount(),
                aggregate.getStatus().name()
        );
    }
}
