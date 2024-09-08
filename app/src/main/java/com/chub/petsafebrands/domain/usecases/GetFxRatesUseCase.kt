package com.chub.petsafebrands.domain.usecases

import com.chub.petsafebrands.data.repo.FxRatesRepository
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import com.chub.petsafebrands.domain.pojo.FxRates
import com.chub.petsafebrands.domain.pojo.UiResult
import java.math.BigDecimal
import javax.inject.Inject

class GetFxRatesUseCase @Inject constructor(private val repository: FxRatesRepository) {
    suspend operator fun invoke(
        base: CurrencyRateItem,
        currencies: List<Currency> = Currency.entries.filter { it != base.currency }
    ): UiResult<FxRates> {

        return when (val result = repository.getRates(base.currency.name, currencies)) {
            is Result.Success -> {
                return if (result.data?.success == true) {
                    UiResult.Success(
                        FxRates(baseRate = CurrencyRateItem(currency = base.currency),
                            rates = result.data.rates.map { (currency, rate) ->
                                CurrencyRateItem(
                                    currency = Currency.valueOf(currency),
                                    coefficient = BigDecimal(rate)
                                )
                            }
                        )
                    )
                } else {
                    UiResult.Failure(result.data?.error?.info ?: "Unknown error")
                }
            }

            is Result.Failure -> UiResult.Failure(result.errorMessage)
            Result.NetworkError -> UiResult.Failure("Network error")
        }
    }
}