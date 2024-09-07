package com.chub.petsafebrands.data.debug

import com.chub.petsafebrands.data.ExchangeRatesResponse
import com.chub.petsafebrands.data.FxRatesRepository
import com.chub.petsafebrands.data.retrofit.Result
import com.google.gson.Gson
import javax.inject.Inject

class FakeFxRatesRepository @Inject constructor(private val apiService: MockApiService) :
    FxRatesRepository {
    override suspend fun getRates(base: String): Result<ExchangeRatesResponse> {
        return apiService.getRates(base)
    }
}