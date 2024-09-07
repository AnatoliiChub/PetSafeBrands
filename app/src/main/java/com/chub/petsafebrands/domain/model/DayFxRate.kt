package com.chub.petsafebrands.domain.model

import java.util.Date

data class DayFxRate(
    val date : Date,
    val rates: List<CurrencyRateItem>,
)