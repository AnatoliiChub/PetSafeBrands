package com.chub.petsafebrands.domain

import com.chub.petsafebrands.data.response.DayRateResponse
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.DayFxRate
import java.math.BigDecimal
import javax.inject.Inject

class DayRateResponseConverter @Inject constructor(private val dateParser: DateParser) {
    fun convert(data: DayRateResponse, baseAmount: BigDecimal = BigDecimal.ZERO): DayFxRate {
        return DayFxRate(
            date = dateParser.parse(data.date),
            rates = data.rates.map { (currency, rate) ->
                CurrencyRateItem(
                    currency = Currency.valueOf(currency),
                    coefficient = BigDecimal(rate),
                    value = BigDecimal(rate) * baseAmount
                )
            }
        )
    }
}