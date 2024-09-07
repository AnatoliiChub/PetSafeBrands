package com.chub.petsafebrands.domain.model

data class FxRates(
    val baseRate: CurrencyRateItem,
    val rates: List<CurrencyRateItem> = emptyList(),
)