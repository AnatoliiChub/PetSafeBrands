package com.chub.petsafebrands.data.debug

import com.chub.petsafebrands.data.repo.FxRatesRepository
import com.chub.petsafebrands.data.response.DayRateResponse
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.DailyRatesQuery
import javax.inject.Inject

class FakeFxRatesRepository @Inject constructor(
    private val apiService: MockApiService
) : FxRatesRepository {
    override suspend fun getRates(base: String, symbols: List<Currency>): Result<DayRateResponse> {
        return apiService.getRates(base)
    }

    override suspend fun getHistoricalRates(request: DailyRatesQuery): Result<DayRateResponse> {
        return apiService.getDailyRates(request.date)
    }
}