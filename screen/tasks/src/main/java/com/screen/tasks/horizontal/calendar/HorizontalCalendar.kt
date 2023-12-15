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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import java.util.Date

@Composable
fun HorizontalCalendar(
    dateRange: State<List<Day>>,
    selectedDay: State<Long>,
    animateScroll: State<Long>,
    modifier: Modifier = Modifier,
    onClickItem: (Long) -> Unit,
) {
    val dateRangeValue by remember(key1 = dateRange) {
        dateRange
    }

    val selectedDayValue by remember(key1 = selectedDay) {
        selectedDay
    }

    val animateScrollValue by remember(key1 = animateScroll) {
        animateScroll
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
                isSelectedDate = it.dateTime == selectedDayValue,
                onClick = onClickItem
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        val screenWidth = with(density) {
            configuration.screenWidthDp.dp.toPx() / 2
        }

        lazyListState.scrollToItem(
            index = dateRangeValue.indexOfFirst { it.dateTime == selectedDayValue } + 1,
            scrollOffset = -screenWidth.toInt()
        )
    }

    LaunchedEffect(key1 = animateScrollValue) {
        val screenWidth = with(density) {
            configuration.screenWidthDp.dp.toPx() / 2
        }

        lazyListState.animateScrollToItem(
            index = dateRangeValue.indexOfFirst { it.dateTime == selectedDayValue } + 1,
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

@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
        var count = 1
        fun date(): Long = Date().time + (++count)
        val dateRange = remember {
            mutableStateOf(
                listOf(
                    Day(
                        dayOfWeek = "Сб",
                        number = "12",
                        dateTime = date(),
                        isToday = false,
                        isAfterToday = false,
                    ),
                    Day(
                        dayOfWeek = "Сб",
                        number = "12",
                        dateTime = date(),
                        isToday = false,
                        isAfterToday = false,
                    ),
                    Day(
                        dayOfWeek = "Сб",
                        number = "12",
                        dateTime = date(),
                        isToday = true,
                        isAfterToday = true,
                    ),
                    Day(
                        dayOfWeek = "Сб",
                        number = "12",
                        dateTime = date(),
                        isToday = false,
                        isAfterToday = true,
                    ),
                    Day(
                        dayOfWeek = "Сб",
                        number = "12",
                        dateTime = date(),
                        isToday = false,
                        isAfterToday = true,
                    ),
                )
            )
        }

        val selectedDay = remember {
            mutableLongStateOf(date() - 1)
        }

        HorizontalCalendar(
            dateRange = dateRange,
            selectedDay = selectedDay,
            animateScroll = selectedDay,
        ) {}
    }
}

