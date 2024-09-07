package com.chub.petsafebrands.ui.screen.rates

import com.chub.petsafebrands.domain.pojo.CurrencyRateItem

sealed class RateScreenAction {
    data class BaseCurrencyChanged(val rate: CurrencyRateItem) : RateScreenAction()
    data class BaseAmountChanged(val amount: String) : RateScreenAction()
    data class RateSelected(val rate: CurrencyRateItem) : RateScreenAction()
}