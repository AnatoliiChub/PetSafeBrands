package com.chub.petsafebrands.ui.screen.rates.state

import com.chub.petsafebrands.domain.pojo.CurrencyRateItem

data class RatesContentState(
    val baseAmount: String = "",
    val currentRate: CurrencyRateItem? = null,
    val rates: List<CurrencyRateItem> = mutableListOf(),
    val selectedRates: List<CurrencyRateItem> = emptyList(),
)