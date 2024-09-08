package com.chub.petsafebrands.domain.usecases

import com.chub.petsafebrands.data.repo.FxRatesRepository
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.DateCalculator
import com.chub.petsafebrands.domain.DayRateResponseConverter
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.DailyRatesQuery
import com.chub.petsafebrands.domain.pojo.DayFxRate
import com.chub.petsafebrands.domain.pojo.FixerApiException
import com.chub.petsafebrands.domain.pojo.UiResult
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetDailyRatesUseCase @Inject constructor(
    private val repository: FxRatesRepository,
    private val dayRateResponseConverter: DayRateResponseConverter,
    private val dateCalculator: DateCalculator
) {
    suspend operator fun invoke(
        base: CurrencyRateItem, currencies: List<Currency>, days: Int
    ): UiResult<List<DayFxRate>> {
        try {
            return coroutineScope {
                val deferredResults = (0..<days).map { day ->
                    val date = dateCalculator.getDateWithOffset(day)
                    val query = DailyRatesQuery(base = base, currencies = currencies, date = date)
                    async { getFxRate(query) }
                }
                return@coroutineScope UiResult.Success(deferredResults.map { it.await() }
                    .sortedByDescending { it.date })
            }
        } catch (e: Exception) {
            return UiResult.Failure(e.message ?: "Unknown error")
        }
    }

    private suspend fun getFxRate(request: DailyRatesQuery): DayFxRate {
        return when (val result = repository.getHistoricalRates(request)) {
            is Result.Success -> {
                if (result.data?.success == true) {
                    dayRateResponseConverter.convert(result.data, request.base.value)
                } else {
                    throw FixerApiException(result.data?.error?.info ?: "Unknown error")
                }
            }

            is Result.Failure -> throw FixerApiException(result.errorMessage)
            Result.NetworkError -> throw FixerApiException("Network error")
        }
    }
}