package com.investment.lockedtermbasedinvestment.admin.domain.policy;

import com.investment.lockedtermbasedinvestment.common.enums.LiquidityPoolStatus;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;

public interface LiquidityPoolStatusPolicy {

    LiquidityPoolStatus evaluate(
            Money totalAmount,
            Money minThreshold
    );
}
