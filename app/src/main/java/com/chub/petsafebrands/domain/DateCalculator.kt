package com.chub.petsafebrands.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateCalculator @Inject constructor() {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
    }

    fun getDateWithOffset(days: Int): String {
        val endDate = Date()
        val startDate = Date(endDate.time - days * 24 * 60 * 60 * 1000)
        return DATE_FORMAT.format(startDate)
    }
}