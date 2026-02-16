package com.investment.lockedtermbasedinvestment.saving.api.controller.cms;

import com.investment.lockedtermbasedinvestment.saving.api.dto.request.LockedProductRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.LockedProductResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.LockedProductService;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cms/locked-products")
@RequiredArgsConstructor
public class CMSLockedProductController {

    private final LockedProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<LockedProductResponse> getProductById(@PathVariable Long id) {
        LockedProductAggregate aggregate = service.findById(id);

        return ResponseEntity.ok(toResponse(aggregate));
    }

    @GetMapping()
    public ResponseEntity<List<LockedProductResponse>> getAllProduct() {
        List<LockedProductResponse> responses = service.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<?> createLockedProduct(
            @RequestBody LockedProductRequest request
            ) {
        LockedProductAggregate aggregate = service.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(aggregate));
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
