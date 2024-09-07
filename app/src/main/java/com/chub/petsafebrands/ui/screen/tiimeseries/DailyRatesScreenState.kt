package com.chub.petsafebrands.ui.screen.tiimeseries

import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.DayFxRate

data class DailyRatesScreenState(
    val contentState: DailyRatesContentState,
    val errorMessage: String = "",
    val isLoading: Boolean = false
)

data class DailyRatesContentState(
    val baseAmount: Float,
    val baseCurrency: Currency,
    val dayRates: List<DayFxRate>,
    val sortBy: SortBy = SortBy.ByDate,
)

sealed class SortBy {
    data object ByDate : SortBy()
    data class ByCurrency(val currency: Currency) : SortBy()
}