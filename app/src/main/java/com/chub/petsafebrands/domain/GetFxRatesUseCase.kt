package com.chub.petsafebrands.domain

import com.chub.petsafebrands.data.FxRatesRepository
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import javax.inject.Inject
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.FxRates
import com.chub.petsafebrands.domain.pojo.UiResult

class GetFxRatesUseCase @Inject constructor(private val repository: FxRatesRepository) {
    suspend operator fun invoke(base: Currency): UiResult<FxRates> {

        return when (val result = repository.getRates(base.name)) {
            is Result.Success -> {
                return UiResult.Success(
                    result.data?.let {
                        FxRates(baseRate = CurrencyRateItem(currency = base, coefficient = 1.0),
                            rates = it.rates.map { (currency, rate) ->
                                CurrencyRateItem(currency = Currency.valueOf(currency), coefficient = rate.toDouble())
                            }
                        )
                    }
                )
            }

            is Result.Failure -> UiResult.Failure(result.errorMessage)
            Result.NetworkError -> UiResult.Failure("Network error")
        }
    }
}