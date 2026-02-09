package com.investment.lockedtembasedinvestment.saving.api.controller.cms;

import com.investment.lockedtembasedinvestment.saving.api.dto.request.LockedProductRequest;
import com.investment.lockedtembasedinvestment.saving.api.dto.response.LockedProductResponse;
import com.investment.lockedtembasedinvestment.saving.application.service.LockedProductService;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cms/locked-products")
@RequiredArgsConstructor
public class CMSLockedProductController {

    private final LockedProductService service;

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
