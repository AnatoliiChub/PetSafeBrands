package com.chub.petsafebrands.ui.screen.daily.state

import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.domain.pojo.SortBy
import java.math.BigDecimal

data class DailyRatesContentState(
    val baseAmount: BigDecimal,
    val baseCurrency: Currency,
    val dayRates: List<DayFxRate>,
    val sortBy: SortBy = SortBy.ByDate,
)