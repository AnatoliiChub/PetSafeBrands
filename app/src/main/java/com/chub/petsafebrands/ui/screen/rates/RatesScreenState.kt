package com.chub.petsafebrands.ui.screen.rates

import com.chub.petsafebrands.UiRate

data class RatesScreenState(
    val isLoading: Boolean = false,
    val baseAmount: String = "",
    val currentRate: UiRate? = null,
    val rates: List<UiRate> = mutableListOf(),
    val selectedRates: List<UiRate> = emptyList()
)