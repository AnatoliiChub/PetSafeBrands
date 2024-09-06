package com.chub.petsafebrands.ui.screen.rates

import com.chub.petsafebrands.UiRate

sealed class RateScreenAction {
    data class BaseRateChanged(val rate: UiRate) : RateScreenAction()
    data class BaseAmountChanged(val amount: String) : RateScreenAction()
    data class RateSelected(val rate: UiRate) : RateScreenAction()
}