package com.chub.petsafebrands.data.response

data class ExchangeRatesResponse(
    val success: Boolean,
    val base: String,
    val rates: Map<String, String>,
)
