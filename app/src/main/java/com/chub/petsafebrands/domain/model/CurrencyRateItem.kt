package com.chub.petsafebrands.domain.model

data class CurrencyRateItem(
    val currency: Currency,
    val coefficient: Double = 1.0,
    val value: Double = 0.0
)

