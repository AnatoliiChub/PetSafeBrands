package com.chub.petsafebrands.domain.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Currency : Parcelable {
    USD, EUR, JPY, GBP, AUD, CAD, CHF, CNY, SEK, NZD
}