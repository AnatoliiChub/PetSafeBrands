package com.chub.petsafebrands.domain

import com.chub.petsafebrands.data.FxRatesRepository
import com.chub.petsafebrands.domain.model.Currency
import com.chub.petsafebrands.domain.model.CurrencyRateItem
import javax.inject.Inject

class GetFxRatesUseCase @Inject constructor(private val repository: FxRatesRepository) {
    suspend operator fun invoke(base: Currency): FxRates {
        val response = repository.getRates(base.name)
        return FxRates(
            baseRate = CurrencyRateItem(Currency.valueOf(response.base), 1.0),
            rates = response.rates.map { (currency, rate) -> CurrencyRateItem(Currency.valueOf(currency), rate.toDouble()) },
        )
    }
}