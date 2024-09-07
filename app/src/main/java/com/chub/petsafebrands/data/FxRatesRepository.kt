package com.chub.petsafebrands.data

import com.chub.petsafebrands.data.retrofit.Result


interface FxRatesRepository {
    suspend fun getRates(base: String): Result<ExchangeRatesResponse>
}