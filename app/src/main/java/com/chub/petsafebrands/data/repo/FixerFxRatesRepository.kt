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
        return fixerApi.getLatestRates(
            apiKey = BuildConfig.API_KEY,
            base = base,
            symbols = symbols.joinToString(",") { it.name })
    }

    override suspend fun getHistoricalRates(request: DailyRatesQuery): Result<DayRateResponse> {
        return fixerApi.getHistoricalRates(
            date = request.date,
            apiKey = BuildConfig.API_KEY,
            base = request.base.currency.name,
            symbols = request.currencies.joinToString(",") { it.name }
        )
    }
}