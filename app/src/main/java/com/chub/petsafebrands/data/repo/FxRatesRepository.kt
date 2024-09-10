package com.chub.petsafebrands.data.repo

import com.chub.petsafebrands.domain.pojo.DailyRatesQuery
import com.chub.petsafebrands.data.response.DayRateResponse
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency


interface FxRatesRepository {
    suspend fun getRates(base: String, symbols : List<Currency>): Result<DayRateResponse>

    suspend fun getHistoricalRates(request: DailyRatesQuery): Result<DayRateResponse>
}