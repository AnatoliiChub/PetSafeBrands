package com.chub.petsafebrands.ui.screen.rates

import com.chub.petsafebrands.domain.model.UiRate

data class RatesScreenState(
    val isLoading: Boolean,
    val error: String,
    val contentState: RatesContentState
)

data class RatesContentState(
    val baseAmount: String = "",
    val currentRate: UiRate? = null,
    val rates: List<UiRate> = mutableListOf(),
    val selectedRates: List<UiRate> = emptyList(),
)