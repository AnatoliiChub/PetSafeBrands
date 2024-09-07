package com.chub.petsafebrands.data.debug

import android.util.Log
import com.chub.petsafebrands.data.DateRangeProvider
import com.chub.petsafebrands.data.FxRatesRepository
import com.chub.petsafebrands.data.RequestTimeSeries
import com.chub.petsafebrands.data.response.ExchangeRatesResponse
import com.chub.petsafebrands.data.response.TimeSeriesResponse
import com.chub.petsafebrands.data.retrofit.Result
import javax.inject.Inject

class FakeFxRatesRepository @Inject constructor(
    private val apiService: MockApiService,
    private val dateRangeProvider: DateRangeProvider
) : FxRatesRepository {
    override suspend fun getRates(base: String): Result<ExchangeRatesResponse> {
        return apiService.getRates(base)
    }

    override suspend fun getDailyRates(request: RequestTimeSeries): Result<TimeSeriesResponse> {
        val result = apiService.getDailyRates()
        Log.d("FakeFxRatesRepository", "getTimeSeriesRates: $result")
        return result
    }
}