package com.investment.lockedtermbasedinvestment.saving.domain.exception;

public class DuplicateDailyInterestException extends RuntimeException {

    private final Long earningId;

    public DuplicateDailyInterestException(Long earningId) {
        super("Duplicate daily interest for earningId=" + earningId);
        this.earningId = earningId;
    }

    public Long getEarningId() {
        return earningId;
    }
}
