package com.investment.lockedtembasedinvestment.admin.domain.policy;

import com.investment.lockedtembasedinvestment.common.enums.LiquidityPoolStatus;
import com.investment.lockedtembasedinvestment.common.sharekernel.Money;

public interface LiquidityPoolStatusPolicy {

    LiquidityPoolStatus evaluate(
            Money totalAmount,
            Money minThreshold
    );
}
