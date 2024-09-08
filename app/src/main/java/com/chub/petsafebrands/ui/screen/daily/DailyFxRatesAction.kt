package com.chub.petsafebrands.ui.screen.daily

import com.chub.petsafebrands.ui.screen.daily.state.SortBy

sealed class DailyFxRatesAction {
    data class SortByChanged(val sortBy: SortBy) : DailyFxRatesAction()
    data object FetchDailyRates : DailyFxRatesAction()
}