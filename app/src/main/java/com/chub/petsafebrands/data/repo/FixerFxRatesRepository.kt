package com.chub.petsafebrands.data.repo

import com.chub.petsafebrands.data.DateRangeProvider
import com.chub.petsafebrands.data.RequestDailyRates
import com.chub.petsafebrands.data.response.DailyRatesResponse
import com.chub.petsafebrands.data.response.ExchangeRatesResponse
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency

class FixerFxRatesRepository(private val dateRangeProvider: DateRangeProvider) : FxRatesRepository {
    override suspend fun getRates(base: String, symbols: List<Currency>): Result<ExchangeRatesResponse> {
        // TODO: Implement real API call
        return Result.Success(
            ExchangeRatesResponse(
                success = true,
                base = "EUR",
                rates = mapOf(
                    "USD" to "1.2",
                    "GBP" to "0.8",
                    "JPY" to "130.0",
                    "AUD" to "1.6",
                    "CAD" to "1.5",
                    "CHF" to "1.1",
                )
            )
        )
    }

    override suspend fun getDailyRates(request: RequestDailyRates): Result<DailyRatesResponse> {
        // TODO: Implement real API call
        return Result.Success(
            DailyRatesResponse(
                success = true,
                timeseries = true,
                base = request.base.currency.name,
                rates = mapOf(
                    "2021-01-01" to mapOf(
                        "USD" to "1.2",
                        "GBP" to "0.8",

                        ),
                    "2021-01-02" to mapOf(
                        "USD" to "1.3",
                        "GBP" to "0.9",

                        ),
                    "2021-01-03" to mapOf(
                        "USD" to "1.4",
                        "GBP" to "1.0",

                        ),
                ),
                start_date = "2021-01-03",
                end_date = "2021-01-08"
            )
        )
    }
}