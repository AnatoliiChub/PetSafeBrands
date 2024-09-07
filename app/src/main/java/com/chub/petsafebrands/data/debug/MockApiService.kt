package com.chub.petsafebrands.data.debug

import com.chub.petsafebrands.config.DebugConfig.MOCK_RESPONSE_HEADER
import com.chub.petsafebrands.data.ExchangeRatesResponse
import com.chub.petsafebrands.data.retrofit.Result
import retrofit2.http.GET
import retrofit2.http.Header

interface MockApiService {

    @GET("latest")
    suspend fun getRates(@Header(MOCK_RESPONSE_HEADER) currency: String): Result<ExchangeRatesResponse>
}