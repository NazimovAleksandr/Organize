package com.dialog.date.calendar


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class CalendarColors(
    val headerTextColor: Color,
    val weekTextColor: Color,
    val dayTextColor: Color,
    val selectedDayTextColor: Color,
    val todayBorder: Color,
    val selectedDayBackground: Color,
) {
    companion object {
        @Composable
        fun default(): CalendarColors {
            return CalendarColors(
                headerTextColor = MaterialTheme.colorScheme.outline,
                weekTextColor = MaterialTheme.colorScheme.onBackground,
                dayTextColor = MaterialTheme.colorScheme.onBackground,
                selectedDayTextColor = MaterialTheme.colorScheme.background,
                todayBorder = MaterialTheme.colorScheme.surface,
                selectedDayBackground = MaterialTheme.colorScheme.surface,
            )
        }
    }
}
