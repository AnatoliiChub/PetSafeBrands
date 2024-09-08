package com.chub.petsafebrands.domain

import com.chub.petsafebrands.data.repo.FxRatesRepository
import com.chub.petsafebrands.data.RequestDailyRates
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.domain.pojo.UiResult
import javax.inject.Inject

class GetDailyRatesUseCase @Inject constructor(
    private val repository: FxRatesRepository,
    private val dateParser: DateParser
) {

    suspend operator fun invoke(request: RequestDailyRates): UiResult<List<DayFxRate>> {
        val baseAmount = request.base.value
        return when (val result = repository.getDailyRates(request)) {
            is Result.Success -> {
                UiResult.Success(
                    result.data?.rates?.map { (date, rates) ->
                        DayFxRate(
                            date = dateParser.parse(date),
                            rates = rates.map { (currency, rate) ->
                                val coefficient = rate.toDouble()
                                CurrencyRateItem(
                                    currency = Currency.valueOf(currency),
                                    coefficient = coefficient,
                                    value = baseAmount * coefficient
                                )

                            }.sortedBy { it.currency })
                    }?.sortedByDescending { it.date } ?: emptyList()
                )
            }

            is Result.Failure -> UiResult.Failure(result.errorMessage)
            Result.NetworkError -> UiResult.Failure("Network error")
        }
    }
}