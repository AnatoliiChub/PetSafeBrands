package com.chub.petsafebrands.data

interface FxRatesRepository {
    suspend fun getRates(base: String): ExchangeRatesResponse
}