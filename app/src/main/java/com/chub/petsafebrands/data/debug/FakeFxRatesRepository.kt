package com.chub.petsafebrands.data.debug

import android.util.Log
import com.chub.petsafebrands.data.RequestDailyRates
import com.chub.petsafebrands.data.repo.FxRatesRepository
import com.chub.petsafebrands.data.response.DailyRatesResponse
import com.chub.petsafebrands.data.response.ExchangeRatesResponse
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency
import javax.inject.Inject

class FakeFxRatesRepository @Inject constructor(
    private val apiService: MockApiService
) : FxRatesRepository {
    override suspend fun getRates(base: String, symbols : List<Currency>): Result<ExchangeRatesResponse> {
        val symbols = symbols.joinToString(",") { it.name }
        Log.d("FakeFxRatesRepository", "getRates: base=$base, symbols=$symbols")
        return apiService.getRates(base)
    }

    override suspend fun getDailyRates(request: RequestDailyRates): Result<DailyRatesResponse> {
        return apiService.getDailyRates()
    }
}