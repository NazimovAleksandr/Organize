package com.screen.tasks.horizontal.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun rememberHorizontalCalendarState() : HorizontalCalendarState {
    return remember {
        HorizontalCalendarState()
    }
}

class HorizontalCalendarState {
    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())

    private val startDateString = "${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR] - 1}"
    private val startDate = dateFormat.parse(startDateString)

    private val endDateString = "${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR] + 1}"
    private val endDate = dateFormat.parse(endDateString)

    private val dates: MutableList<String> = mutableListOf()
    private var curTime = startDate?.time ?: 0L
    private val endTime = endDate?.time ?: 0L
    private val interval = 24 * 1000 * 60 * 60L

    init {
        while (curTime <= endTime) {
            dates.add(dateFormat.format(curTime))
            curTime += interval
        }
    }

    val dateRange: List<String> = dates.toList()
    val currentDateIndex: Int = dates.indexOfFirst { dateFormat.format(System.currentTimeMillis()) == it } + 1
}