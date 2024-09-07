package com.chub.petsafebrands.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DateRangeProvider @Inject constructor() {
    companion object {
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
    }

    /**
     * returns a pair of dates in the format "yyyy-MM-dd" that
     * are [days] apart from each other starting from today
     */
    fun getDates(days: Int): Pair<String, String> {
        val endDate = Date()
        val startDate = Date(endDate.time - days * 24 * 60 * 60 * 1000)
        return Pair(DATE_FORMAT.format(startDate), DATE_FORMAT.format(endDate))
    }
}