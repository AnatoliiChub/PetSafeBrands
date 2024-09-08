package com.chub.petsafebrands.ui.screen.rates

import com.chub.petsafebrands.domain.pojo.CurrencyRateItem

sealed class FxRatesAction {
    data class BaseCurrencyChanged(val rate: CurrencyRateItem) : FxRatesAction()
    data class BaseAmountChanged(val amount: String) : FxRatesAction()
    data class FxRatesSelected(val rate: CurrencyRateItem) : FxRatesAction()
    data object FetchRates : FxRatesAction()
}