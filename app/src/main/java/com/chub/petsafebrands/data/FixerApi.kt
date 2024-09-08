package com.chub.petsafebrands.data

import com.chub.petsafebrands.data.response.DayRateResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.chub.petsafebrands.data.retrofit.Result
import retrofit2.http.Path

interface FixerApi {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") apiKey: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Result<DayRateResponse>

    @GET("{date}")
    suspend fun getHistoricalRates(
        @Path("date") date: String,
        @Query("access_key") apiKey: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Result<DayRateResponse>
}