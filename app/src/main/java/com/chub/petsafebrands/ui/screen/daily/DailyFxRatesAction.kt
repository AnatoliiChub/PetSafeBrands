package com.chub.petsafebrands.ui.screen.daily

import com.chub.petsafebrands.domain.pojo.SortBy

sealed class DailyFxRatesAction {
    data class SortByChanged(val sortBy: SortBy) : DailyFxRatesAction()
    data object FetchDailyRates : DailyFxRatesAction()
}