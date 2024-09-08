package com.chub.petsafebrands.ui.screen.tiimeseries

sealed class DailyFxRatesAction {
    data class SortByChanged(val sortBy: SortBy) : DailyFxRatesAction()
    data object FetchDailyRates : DailyFxRatesAction()
}