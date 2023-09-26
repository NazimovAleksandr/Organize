package com.dialog.date.calendar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import java.time.LocalDate
import java.time.Month

@Composable
fun Calendar(
    currentDay: LocalDate?,
    calendarType: CalendarType,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
    calendarHeaderTextStyle: TextStyle? = null,
    colors: CalendarColors = CalendarColors.default(),
    dayStyle: TextStyle = MaterialTheme.typography.titleMedium,
    selectedDayStyle: TextStyle = MaterialTheme.typography.titleMedium,
    weekDayStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    headerContent: @Composable ((Month, Int) -> Unit)? = null,
    onDayClick: (LocalDate) -> Unit = {},
) {
    when (calendarType) {
        CalendarType.Month -> {
            CalendarMonth(
                currentDay = currentDay,
                modifier = modifier,
                showLabel = showLabel,
                calendarHeaderTextStyle = calendarHeaderTextStyle,
                colors = colors,
                dayStyle = dayStyle,
                selectedDayStyle = selectedDayStyle,
                weekDayStyle = weekDayStyle,
                headerContent = headerContent,
                onDayClick = onDayClick,
            )
        }

        CalendarType.Week -> {
            CalendarWeek(
                modifier = modifier,
                currentDay = currentDay,
                showLabel = showLabel,
                calendarHeaderTextStyle = calendarHeaderTextStyle,
                colors = colors,
                dayStyle = dayStyle,
                selectedDayStyle = selectedDayStyle,
                weekDayStyle = weekDayStyle,
                headerContent = headerContent,
                onDayClick = onDayClick,
            )
        }
    }
}
