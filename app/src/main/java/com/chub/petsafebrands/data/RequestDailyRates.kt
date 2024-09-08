package com.chub.petsafebrands.data

import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem

data class RequestDailyRates(
    val base : CurrencyRateItem,
    val currencies: List<Currency>,
    val days : Int = 5
)