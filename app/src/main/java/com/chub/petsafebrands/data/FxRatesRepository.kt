package com.chub.petsafebrands.data

import com.chub.petsafebrands.data.response.ExchangeRatesResponse
import com.chub.petsafebrands.data.response.TimeSeriesResponse
import com.chub.petsafebrands.data.retrofit.Result


interface FxRatesRepository {
    suspend fun getRates(base: String): Result<ExchangeRatesResponse>

    suspend fun getDailyRates(request: RequestTimeSeries): Result<TimeSeriesResponse>
}