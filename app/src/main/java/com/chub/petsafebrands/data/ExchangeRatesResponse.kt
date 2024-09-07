package com.chub.petsafebrands.data

data class ExchangeRatesResponse(
    val success: Boolean,
    val base: String,
    val rates: Map<String, String>,
)

data class ErrorResponse(
    val error: ErrorDetails
)

data class ErrorDetails(
    val code: Int = 0,
    val info: String = ""
)