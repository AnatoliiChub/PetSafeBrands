package com.chub.petsafebrands.ui.screen.daily.state

import com.chub.petsafebrands.ui.screen.ErrorState

data class DailyRatesScreenState(
    val contentState: DailyRatesContentState,
    val error : ErrorState = ErrorState.None,
    val isLoading: Boolean = false
)



