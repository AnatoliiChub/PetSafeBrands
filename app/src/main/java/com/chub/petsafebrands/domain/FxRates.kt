package com.chub.petsafebrands.domain

import com.chub.petsafebrands.domain.model.UiRate

data class FxRates(
    val baseRate: UiRate? = null,
    val rates: List<UiRate> = emptyList(),
)