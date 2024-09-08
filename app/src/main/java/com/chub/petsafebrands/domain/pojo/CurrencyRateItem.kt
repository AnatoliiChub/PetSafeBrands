package com.chub.petsafebrands.domain.pojo

import java.math.BigDecimal

data class CurrencyRateItem(
    val currency: Currency,
    val coefficient: BigDecimal = BigDecimal.ONE,
    val value: BigDecimal = BigDecimal.ZERO
)

