package com.chub.petsafebrands.domain.usecases

import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.domain.pojo.SortBy
import java.math.BigDecimal
import javax.inject.Inject

class DescendingSortUseCase @Inject constructor() {

    operator fun invoke(list: List<DayFxRate>, sortBy: SortBy): List<DayFxRate> {
        return when (sortBy) {
            SortBy.ByDate -> list.sortedByDescending { it.date }
            is SortBy.ByCurrency -> list.sortedByDescending { day ->
                day.rates.find { it.currency == sortBy.currency }?.value ?: BigDecimal.ZERO
            }
        }
    }
}
