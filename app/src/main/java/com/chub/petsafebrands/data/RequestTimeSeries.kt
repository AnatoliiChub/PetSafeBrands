package com.chub.petsafebrands.data

import com.chub.petsafebrands.domain.model.Currency
import com.chub.petsafebrands.domain.model.CurrencyRateItem

data class RequestTimeSeries(
    val base : CurrencyRateItem,
    val currencies: List<Currency>,
    val days : Int = 5
)