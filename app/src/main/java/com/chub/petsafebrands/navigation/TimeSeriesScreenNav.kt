package com.chub.petsafebrands.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TimeSeriesScreenNav(
    val baseCurrency: Int,
    val baseAmount: String,
    val currencies: List<Int>
) : Parcelable
