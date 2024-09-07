package com.chub.petsafebrands.ui.screen.tiimeseries

import com.chub.petsafebrands.domain.model.DayFxRate
import com.chub.petsafebrands.domain.model.Currency

data class DailySeriesScreenState(
    val baseAmount: Float,
    val baseCurrency: Currency,
    val dayRates: List<DayFxRate>,
    val errorMessage: String = "",
    val isLoading : Boolean = false
)