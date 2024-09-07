package com.chub.petsafebrands.domain.model

data class CurrencyRateItem(
    val currency: Currency,
    val coefficient: Double,
    val value: Double = 0.0
)

