package com.chub.petsafebrands.domain.pojo

/**
 * Request for daily rates
 * @param base - base currency
 * @param currencies - list of currencies to get rates for
 * @param date - date of rates
 */
data class DailyRatesQuery(
    val base: CurrencyRateItem,
    val currencies: List<Currency>,
    val date: String
)