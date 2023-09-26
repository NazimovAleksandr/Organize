package com.dialog.date.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.todayIn
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun CalendarWeek(
    modifier: Modifier = Modifier,
    currentDay: LocalDate?,
    showLabel: Boolean,
    calendarHeaderTextStyle: androidx.compose.ui.text.TextStyle?,
    colors: CalendarColors,
    dayStyle: androidx.compose.ui.text.TextStyle,
    selectedDayStyle: androidx.compose.ui.text.TextStyle,
    weekDayStyle: androidx.compose.ui.text.TextStyle,
    headerContent: @Composable ((Month, Int) -> Unit)?,
    onDayClick: (LocalDate) -> Unit,
    labelFormat: (DayOfWeek) -> String = {
        it.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        )
    },
) {
    val today = currentDay?.toKotlinLocalDate() ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
    val weekValue = remember { mutableStateOf(today.getNext7Dates()) }
    val yearAndMonth = getCurrentMonthAndYear(weekValue.value)
    var selectedDate by remember { mutableStateOf(today) }

    val newHeaderTextStyle: androidx.compose.ui.text.TextStyle =
        calendarHeaderTextStyle ?: MaterialTheme.typography.titleSmall

    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        if (headerContent != null) {
            headerContent(yearAndMonth.first, yearAndMonth.second)
        } else {
            CalendarHeader(
                month = yearAndMonth.first,
                year = yearAndMonth.second,
                textStyle = newHeaderTextStyle,
                colors = colors,
                onPreviousClick = {
                    val firstDayOfDisplayedWeek = weekValue.value.first()
                    weekValue.value = firstDayOfDisplayedWeek.getPrevious7Dates()
                },
                onNextClick = {
                    val lastDayOfDisplayedWeek = weekValue.value.last().plus(1, DateTimeUnit.DAY)
                    weekValue.value = lastDayOfDisplayedWeek.getNext7Dates()
                },
            )
        }

        /*LazyRow(
            modifier = Modifier.fillMaxWidth(),
            content = {
                itemsIndexed() { _, item ->

                }
            }
        )*/

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (showLabel) {
                /*Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    weekValue.value.forEach { item ->
                        Text(
                            modifier = Modifier,
                            style = weekDayStyle,
                            color = colors.weekTextColor,
                            text = labelFormat(item.dayOfWeek),
                            textAlign = TextAlign.Center
                        )
                    }
                }*/
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 36.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    weekValue.value.forEach { item ->
                        Box(
                            contentAlignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .weight(weight = 1f)
                        ) {
                            Text(
                                style = weekDayStyle,
                                color = colors.weekTextColor,
                                text = labelFormat(item.dayOfWeek).lowercase(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }

            key(weekValue.value) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    weekValue.value.forEach { item ->
                        CalendarDay(
                            date = item,
                            colors = colors,
                            dayStyle = dayStyle,
                            selectedDayStyle = selectedDayStyle,
                            selectedDate = selectedDate,
                            onDayClick = { clickedDate ->
                                selectedDate = clickedDate
                                onDayClick(clickedDate.toJavaLocalDate())
                            },
                        )
                    }
                }
            }
        }
    }
}

private fun getCurrentMonthAndYear(weekValue: List<kotlinx.datetime.LocalDate>): Pair<Month, Int> {
    val month = weekValue.first().month
    val year = weekValue.first().year
    return Pair(month, year)
}
