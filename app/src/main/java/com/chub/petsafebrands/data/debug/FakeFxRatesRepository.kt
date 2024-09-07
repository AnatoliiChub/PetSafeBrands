package com.chub.petsafebrands.data.debug

import com.chub.petsafebrands.data.ErrorResponse
import com.chub.petsafebrands.data.ExchangeRatesResponse
import com.chub.petsafebrands.data.FixerApiException
import com.chub.petsafebrands.data.FxRatesRepository
import com.google.gson.Gson
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class FakeFxRatesRepository @Inject constructor(private val apiService: MockApiService, val gson: Gson) :
    FxRatesRepository {
    override suspend fun getRates(base: String): ExchangeRatesResponse {
        try {
            val response = apiService.getRates(base)
            return if (response.isSuccessful) response.body()!!
            else {
                val error = gson.fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                throw FixerApiException(error.error.info)
            }
            // TODO: EXTRACT THIS TO A SEPARATE PLACE
        } catch (exception: SocketTimeoutException) {
            throw FixerApiException("Timeout error")
        } catch (exception: IOException) {
            throw FixerApiException("Network error")
        } catch (exception: Exception) {
            throw FixerApiException("Unknown error")
        }
    }
}