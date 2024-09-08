package com.chub.petsafebrands.data.repo

import com.chub.petsafebrands.BuildConfig
import com.chub.petsafebrands.data.FixerApi
import com.chub.petsafebrands.data.response.DayRateResponse
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.DailyRatesQuery

class FixerFxRatesRepository(
    private val fixerApi: FixerApi,
) : FxRatesRepository {
    override suspend fun getRates(base: String, symbols: List<Currency>): Result<DayRateResponse> {
        val r = fixerApi.getLatestRates(BuildConfig.API_KEY, base, symbols.joinToString(",") { it.name })
        return r
    }

    override suspend fun getHistoricalRates(request: DailyRatesQuery): Result<DayRateResponse> {
        return Result.Failure(111,"Not implemented")
    }
}