package com.chub.petsafebrands.ui.screen.daily.state

import com.chub.petsafebrands.domain.pojo.Currency

sealed class SortBy {
    data object ByDate : SortBy()
    data class ByCurrency(val currency: Currency) : SortBy()
}