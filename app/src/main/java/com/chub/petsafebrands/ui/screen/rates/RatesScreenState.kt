package com.chub.petsafebrands.ui.screen.rates

import com.chub.petsafebrands.domain.model.CurrencyRateItem

data class RatesScreenState(
    val isLoading: Boolean,
    val error: String,
    val contentState: RatesContentState
)

data class RatesContentState(
    val baseAmount: String = "",
    val currentRate: CurrencyRateItem? = null,
    val rates: List<CurrencyRateItem> = mutableListOf(),
    val selectedRates: List<CurrencyRateItem> = emptyList(),
)