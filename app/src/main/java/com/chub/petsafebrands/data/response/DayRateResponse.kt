package com.chub.petsafebrands.data.response

data class DayRateResponse(
    val success: Boolean,
    val date: String,
    val base: String,
    val rates: Map<String, String>,
    val error: ErrorDetails?
)
