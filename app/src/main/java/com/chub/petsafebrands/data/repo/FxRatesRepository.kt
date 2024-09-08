package com.chub.petsafebrands.data.repo

import com.chub.petsafebrands.data.RequestDailyRates
import com.chub.petsafebrands.data.response.ExchangeRatesResponse
import com.chub.petsafebrands.data.response.DailyRatesResponse
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency


interface FxRatesRepository {
    suspend fun getRates(base: String, symbols : List<Currency>): Result<ExchangeRatesResponse>

    suspend fun getDailyRates(request: RequestDailyRates): Result<DailyRatesResponse>
}