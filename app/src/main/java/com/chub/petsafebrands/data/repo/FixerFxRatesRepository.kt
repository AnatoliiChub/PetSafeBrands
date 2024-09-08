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
        return fixerApi.getLatestRates(BuildConfig.API_KEY, base, symbols.joinToString(",") { it.name })
    }

    override suspend fun getHistoricalRates(request: DailyRatesQuery): Result<DayRateResponse> {
        return fixerApi.getHistoricalRates(
            BuildConfig.API_KEY,
            request.date,
            request.base.currency.name,
            request.currencies.joinToString(",") { it.name }
        )
    }
}