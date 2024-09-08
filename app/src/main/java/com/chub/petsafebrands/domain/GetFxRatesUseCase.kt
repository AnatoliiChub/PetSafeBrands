package com.chub.petsafebrands.domain

import com.chub.petsafebrands.data.repo.FxRatesRepository
import com.chub.petsafebrands.domain.pojo.Currency
import com.chub.petsafebrands.domain.pojo.CurrencyRateItem
import javax.inject.Inject
import com.chub.petsafebrands.data.retrofit.Result
import com.chub.petsafebrands.domain.pojo.FxRates
import com.chub.petsafebrands.domain.pojo.UiResult
import java.math.BigDecimal

class GetFxRatesUseCase @Inject constructor(private val repository: FxRatesRepository) {
    suspend operator fun invoke(base: Currency, currencies : List<Currency> = Currency.entries): UiResult<FxRates> {

        return when (val result = repository.getRates(base.name, currencies)) {
            is Result.Success -> {
                return UiResult.Success(
                    result.data?.let {
                        FxRates(baseRate = CurrencyRateItem(currency = base),
                            rates = it.rates.map { (currency, rate) ->
                                CurrencyRateItem(currency = Currency.valueOf(currency), coefficient = BigDecimal(rate))
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