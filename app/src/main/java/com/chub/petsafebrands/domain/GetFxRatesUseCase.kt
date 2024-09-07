package com.chub.petsafebrands.domain

import com.chub.petsafebrands.data.FxRatesRepository
import com.chub.petsafebrands.domain.model.Currency
import com.chub.petsafebrands.domain.model.UiRate
import javax.inject.Inject

class GetFxRatesUseCase @Inject constructor(private val repository: FxRatesRepository) {
    suspend operator fun invoke(base: Currency): FxRates {
        val response = repository.getRates(base.name)
        return FxRates(
            baseRate = UiRate(Currency.valueOf(response.base), 1.0),
            rates = response.rates.map { (currency, rate) -> UiRate(Currency.valueOf(currency), rate.toDouble()) },
        )
    }
}