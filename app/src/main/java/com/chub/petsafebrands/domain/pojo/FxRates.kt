package com.chub.petsafebrands.domain.pojo

data class FxRates(
    val baseRate: CurrencyRateItem,
    val rates: List<CurrencyRateItem> = emptyList(),
)