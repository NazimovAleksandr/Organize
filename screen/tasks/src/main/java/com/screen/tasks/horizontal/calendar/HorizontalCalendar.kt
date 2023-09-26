package com.screen.tasks.horizontal.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.ui.composable.Content
import com.core.ui.modifier.clickableSingle
import com.core.ui.theme.OrganizeTheme
import com.core.ui.theme.PriorityCardNotAssigned
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HorizontalCalendar(
//    dateRange: State<List<Day>>,
    modifier: Modifier = Modifier,
    onClickItem: (Long) -> Unit,
) {
    var dateRangeValue: List<Day> by remember {
        mutableStateOf(getDatesRange())
    }

    val lazyListState = rememberLazyListState()

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    LazyRow(
        state = lazyListState,
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(items = dateRangeValue, key = { it.dateTime }) {
            DayItem(
                day = it,
                isSelectedDate = it.isSelected,
                onClick = { date ->
                    onClickItem.invoke(date)
                    dateRangeValue = dateRangeValue.selectDate(date)
                }
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        val screenWidth = with(density) {
            configuration.screenWidthDp.dp.toPx() / 2
        }

        lazyListState.scrollToItem(
            index = dateRangeValue.indexOfFirst { it.isSelected } + 1,
            scrollOffset = -screenWidth.toInt()
        )
    }
}

@Composable
private fun DayItem(
    day: Day,
    isSelectedDate: Boolean,
    onClick: (Long) -> Unit,
) {
    val dayNumberModifier = Modifier
        .size(size = 20.dp)
        .clip(shape = CircleShape)
        .let {
            if (day.isToday) it.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) else it
        }
        .let {
            if (isSelectedDate) it.background(
                color = MaterialTheme.colorScheme.primary
            ) else it
        }

    Content {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 4.dp, bottom = 2.dp)
                .clip(shape = CircleShape)
                .clickableSingle { onClick.invoke(day.dateTime) }
                .padding(horizontal = 6.dp)
                .padding(bottom = 4.dp)
        ) {
            Text(
                text = day.dayOfWeek,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(top = 2.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = dayNumberModifier
            ) {
                Text(
                    text = day.number,
                    style = MaterialTheme.typography.titleSmall,
                    color = when {
                        isSelectedDate -> MaterialTheme.colorScheme.background
                        day.isAfterToday -> MaterialTheme.colorScheme.onBackground
                        else -> PriorityCardNotAssigned
                    },
                )
            }
        }
    }
}

private fun List<Day>.selectDate(date: Long): List<Day> {
    return map {
        when {
            it.dateTime == date && it.isSelected -> it

            it.isSelected -> it.copy(isSelected = false)
            it.dateTime == date -> it.copy(isSelected = true)

            else -> it
        }
    }
}

private fun getDatesRange(): List<Day> {
    val calendar: Calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())

    val startDateString =
        "${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR] - 1}"
    val startDate = dateFormat.parse(startDateString)

    val endDateString =
        "${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR] + 1}"
    val endDate = dateFormat.parse(endDateString)

    val dates: MutableList<Day> = mutableListOf()
    var curTime = startDate?.time ?: 0L
    val endTime = endDate?.time ?: 0L
    val interval = 24 * 1000 * 60 * 60L

    val dateFormatDayOfWeek = SimpleDateFormat("EE", Locale.getDefault())
    val dateFormatDayNumber = SimpleDateFormat("dd", Locale.getDefault())

    val today = System.currentTimeMillis() - 24 * 1000 * 60 * 60L

    val currentDate = Date()
    val sdf = SimpleDateFormat("ddMMyyyy", Locale.getDefault())

    while (curTime <= endTime) {
        val dayOfWeek = dateFormatDayOfWeek.format(curTime).replaceFirstChar { it.titlecaseChar() }
        val dayNumber = dateFormatDayNumber.format(curTime) ?: ""

        val dateTime = curTime

        dates.add(
            Day(
                dayOfWeek = dayOfWeek,
                number = dayNumber,
                dateTime = curTime,
                isSelected = sdf.format(currentDate).toInt() == sdf.format(dateTime).toInt(),
                isToday = sdf.format(currentDate).toInt() == sdf.format(dateTime).toInt(),
                isAfterToday = today <= dateTime,
            )
        )

        curTime += interval
    }

    return dates
}

@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
//        val state = remember {
//            mutableLongStateOf(Date().time)
//        }
//        HorizontalCalendar(
//            selectedDate = state
//        ) {}
    }
}

