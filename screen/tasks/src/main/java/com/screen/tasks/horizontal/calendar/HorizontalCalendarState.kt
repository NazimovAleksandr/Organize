package com.screen.tasks.horizontal.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun rememberHorizontalCalendarState(): HorizontalCalendarState {
    return remember {
        HorizontalCalendarState()
    }
}

class HorizontalCalendarState {
    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())

    private val startDateString =
        "${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR] - 1}"
    private val startDate = dateFormat.parse(startDateString)

    private val endDateString =
        "${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR] + 1}"
    private val endDate = dateFormat.parse(endDateString)

    private val dates: MutableList<Day> = mutableListOf()
    private var curTime = startDate?.time ?: 0L
    private val endTime = endDate?.time ?: 0L
    private val interval = 24 * 1000 * 60 * 60L

    private val dateFormatDayOfWeek = SimpleDateFormat("EE", Locale.getDefault())
    private val dateFormatDayNumber = SimpleDateFormat("dd", Locale.getDefault())

    init {
        val today = System.currentTimeMillis() - 24 * 1000 * 60 * 60L

        val currentDate = Date()
        val sdf = SimpleDateFormat("ddMMyyyy", Locale.getDefault())

        while (curTime <= endTime) {
            val dayOfWeek = dateFormatDayOfWeek.format(curTime).replaceFirstChar { it.titlecase(Locale.getDefault()) }
            val dayNumber = dateFormatDayNumber.format(curTime) ?: ""

            val dateTime = curTime

            dates.add(
                Day(
                    dayOfWeek = dayOfWeek,
                    number = dayNumber,
                    dateTime = curTime,
                    isToday = sdf.format(currentDate).toInt() == sdf.format(dateTime).toInt(),
                    isAfterToday = today <= dateTime,
                )
            )

            curTime += interval
        }
    }

    val dateRange: List<Day> = dates.toList()
    val currentDateIndex: Int = dates.indexOfFirst {
        dateFormat.format(System.currentTimeMillis()) == dateFormat.format(it.dateTime)
    } + 1
}

class Day(
    val dayOfWeek: String,
    val number: String,
    val dateTime: Long,
    val isToday: Boolean,
    val isAfterToday: Boolean,
)