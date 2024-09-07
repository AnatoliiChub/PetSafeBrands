package com.chub.petsafebrands.data.debug

import com.chub.petsafebrands.data.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

const val MOCK_RESPONSE_HEADER: String = "X-Mock-Response-File"

const val SUCCESS_CODE = 200

interface MockApiService {

    @GET("latest")
    suspend fun getRates(@Header(MOCK_RESPONSE_HEADER) currency: String): Response<ExchangeRatesResponse>
}