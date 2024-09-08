package com.chub.petsafebrands.domain

import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.ui.screen.daily.state.SortBy
import javax.inject.Inject

class DescendingSortUseCase @Inject constructor() {

    operator fun invoke(list: List<DayFxRate>, sortBy: SortBy): List<DayFxRate> {
        return when(sortBy) {
            SortBy.ByDate -> list.sortedByDescending { it.date }
            is SortBy.ByCurrency -> list.sortedByDescending { day ->
                day.rates.find { it.currency == sortBy.currency }?.value ?: 0.0
            }
        }
    }
}