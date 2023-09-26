package com.dialog.date.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.todayIn
import java.time.LocalDate
import java.util.Locale

private val weekDays
    get() = listOf(
        DayOfWeek.MONDAY.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
        DayOfWeek.TUESDAY.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
        DayOfWeek.WEDNESDAY.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
        DayOfWeek.THURSDAY.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
        DayOfWeek.FRIDAY.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
        DayOfWeek.SATURDAY.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
        DayOfWeek.SUNDAY.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
    )

@Composable
internal fun CalendarMonth(
    currentDay: LocalDate?,
    modifier: Modifier,
    showLabel: Boolean,
    calendarHeaderTextStyle: TextStyle?,
    colors: CalendarColors,
    dayStyle: TextStyle,
    selectedDayStyle: TextStyle,
    weekDayStyle: TextStyle,
    headerContent: @Composable ((Month, Int) -> Unit)?,
    onDayClick: (LocalDate) -> Unit,
) {
    val today = currentDay?.toKotlinLocalDate() ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
    var selectedDate by remember(key1 = today) { mutableStateOf(today) }
    var displayedMonth by remember { mutableStateOf(today.month) }
    var displayedYear by remember { mutableIntStateOf(today.year) }
    val currentMonth = displayedMonth
    val currentYear = displayedYear

    val newHeaderTextStyle: TextStyle = calendarHeaderTextStyle ?: MaterialTheme.typography.titleSmall

    val daysInMonth = currentMonth.length(currentYear.isLeapYear())
    val monthValue = currentMonth.value.toString().padStart(2, '0')
    val startDayOfMonth = "$currentYear-$monthValue-01".toLocalDate()
    val firstDayOfMonth = startDayOfMonth.dayOfWeek

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        if (headerContent != null) {
            headerContent(currentMonth, currentYear)
        } else {
            CalendarHeader(
                month = currentMonth,
                year = currentYear,
                textStyle = newHeaderTextStyle,
                colors = colors,
                onPreviousClick = {
                    displayedYear -= if (currentMonth == Month.JANUARY) 1 else 0
                    displayedMonth -= 1
                },
                onNextClick = {
                    displayedYear += if (currentMonth == Month.DECEMBER) 1 else 0
                    displayedMonth += 1
                },
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(space = 2.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .aspectRatio(ratio = 3f / 2.3f)
        ) {
            if (showLabel) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 36.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    weekDays.forEach { item ->
                        Box(
                            contentAlignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .weight(weight = 1f)
                        ) {
                            Text(
                                style = weekDayStyle,
                                color = colors.weekTextColor,
                                text = item.lowercase(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }

            getDaysOfMonth(firstDayOfMonth, daysInMonth)
                .withIndex()
                .groupBy { it.index / 7 }
                .map { it.value.map { value -> value.value } }
                .forEach { row ->
                    key(row) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .weight(weight = 1f)
                        ) {
                            row.forEach { item ->
                                if (item > 0) {
                                    val day = calculateDay(item, currentMonth, currentYear)

                                    CalendarDay(
                                        date = day,
                                        colors = colors,
                                        dayStyle = dayStyle,
                                        selectedDayStyle = selectedDayStyle,
                                        selectedDate = selectedDate,
                                        onDayClick = { clickedDate ->
                                            selectedDate = clickedDate
                                            onDayClick(clickedDate.toJavaLocalDate())
                                        },
                                    )
                                } else {
                                    CalendarDay(
                                        date = LocalDate.now().toKotlinLocalDate(),
                                        colors = colors,
                                        dayStyle = dayStyle,
                                        selectedDayStyle = selectedDayStyle,
                                        selectedDate = selectedDate,
                                        onDayClick = {},
                                        modifier = Modifier
                                            .alpha(alpha = 0f)
                                    )
                                }
                            }
                        }
                    }
                }
        }
        /*LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (showLabel) {
                items(weekDays) { item ->
                    Text(
                        modifier = Modifier,
                        style = weekDayStyle,
                        color = colors.weekTextColor,
                        text = item.lowercase(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(
                items = (getFirstDayOfMonth(firstDayOfMonth)..daysInMonth).toList(),
                key = {
                    when {
                        it > 0 -> calculateDay(it, currentMonth, currentYear).toString()
                        else -> it.toString()
                    }
                }
            ) {
                if (it > 0) {
                    val day = calculateDay(it, currentMonth, currentYear)

                    CalendarDay(
                        date = day,
                        colors = colors,
                        dayStyle = dayStyle,
                        selectedDayStyle = selectedDayStyle,
                        selectedDate = selectedDate,
                        onDayClick = { clickedDate ->
                            selectedDate = clickedDate
                            onDayClick(clickedDate)
                        },
                    )
                }
            }
        }*/
    }
}

private fun getFirstDayOfMonth(firstDayOfMonth: DayOfWeek) = -(firstDayOfMonth.value).minus(2)

private fun getDaysOfMonth(
    firstDayOfMonth: DayOfWeek,
    daysInMonth: Int,
): List<Int> {
    val list = (getFirstDayOfMonth(firstDayOfMonth)..daysInMonth).toMutableList()

    while (list.size / 7f < 5f) {
        list.add(-list.size)
    }

    while (list.size / 7f > 5f && list.size / 7f < 6f) {
        list.add(-list.size)
    }

    return list
}

private fun calculateDay(day: Int, currentMonth: Month, currentYear: Int): kotlinx.datetime.LocalDate {
    val monthValue = currentMonth.value.toString().padStart(2, '0')
    val dayValue = day.toString().padStart(2, '0')
    return "$currentYear-$monthValue-$dayValue".toLocalDate()
}

