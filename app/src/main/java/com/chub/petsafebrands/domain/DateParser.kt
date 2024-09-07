package com.chub.petsafebrands.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateParser @Inject constructor() {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val DATE_UI_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    }

    fun parse(date: String): Date {
        return DATE_FORMAT.parse(date)!!
    }
}

fun Date.toFormattedString(): String {
    return DateParser.DATE_UI_FORMAT.format(this)
}