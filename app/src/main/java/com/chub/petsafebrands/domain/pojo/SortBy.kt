package com.chub.petsafebrands.domain.pojo

sealed class SortBy {
    data object ByDate : SortBy()
    data class ByCurrency(val currency: Currency) : SortBy()
}