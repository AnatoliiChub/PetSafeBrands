package com.chub.petsafebrands.domain

import com.chub.petsafebrands.domain.model.CurrencyRateItem

data class FxRates(
    val baseRate: CurrencyRateItem,
    val rates: List<CurrencyRateItem> = emptyList(),
)