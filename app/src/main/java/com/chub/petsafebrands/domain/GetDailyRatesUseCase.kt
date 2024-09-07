package com.chub.petsafebrands.domain

import com.chub.petsafebrands.data.FxRatesRepository
import com.chub.petsafebrands.data.RequestTimeSeries
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.model.Currency
import com.chub.petsafebrands.domain.model.CurrencyRateItem
import com.chub.petsafebrands.domain.model.DayFxRate
import com.chub.petsafebrands.domain.model.UiResult
import javax.inject.Inject

class GetDailyRatesUseCase @Inject constructor(
    private val repository: FxRatesRepository,
    private val dateParser: DateParser
) {

    suspend operator fun invoke(request: RequestTimeSeries): UiResult<List<DayFxRate>> {
        val baseAmount = request.base.value
        return when (val result = repository.getDailyRates(request)) {
            is Result.Success -> {
                UiResult.Success(
                    result.data?.rates?.map { (date, rates) ->
                        DayFxRate(
                            date = dateParser.parse(date),
                            rates = rates.map { (currency, rate) ->
                                val coefficient = rate.toDouble()
                                val item = CurrencyRateItem(
                                    currency = Currency.valueOf(currency),
                                    coefficient = coefficient,
                                    value = baseAmount * coefficient
                                )
                                return@map item
                            }.sortedBy { it.currency })
                    }?.sortedByDescending { it.date } ?: emptyList()
                )
            }

            is Result.Failure -> UiResult.Failure(result.errorMessage)
            Result.NetworkError -> UiResult.Failure("Network error")
        }
    }
}