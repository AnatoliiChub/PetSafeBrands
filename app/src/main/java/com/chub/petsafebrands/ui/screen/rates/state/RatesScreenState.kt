package com.chub.petsafebrands.ui.screen.rates.state

import com.chub.petsafebrands.ui.screen.ErrorState

data class RatesScreenState(
    val isLoading: Boolean,
    val error: ErrorState = ErrorState.None,
    val contentState: RatesContentState
)

