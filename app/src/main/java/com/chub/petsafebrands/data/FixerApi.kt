package com.chub.petsafebrands.data

import com.chub.petsafebrands.data.response.DayRateResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.chub.petsafebrands.data.retrofit.Result

interface FixerApi {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") apiKey: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Result<DayRateResponse>

    @GET("timeseries")
    suspend fun getTimeSeriesRates(
        @Query("access_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Result<DayRateResponse>
}