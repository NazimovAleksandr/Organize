package com.screen.tasks.horizontal.calendar

import android.util.Log
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
import com.core.ui.theme.PriorityNotAssigned
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HorizontalCalendar(
    selectedDate: Long,
    modifier: Modifier = Modifier,
    state: HorizontalCalendarState = rememberHorizontalCalendarState(),
    onClickItem: (Long) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val dateFormat = remember { SimpleDateFormat("dd/M/yyyy", Locale.getDefault()) }

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    LazyRow(
        state = lazyListState,
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(items = state.dateRange, key = { it }) {
            Log.d("TAG", "HorizontalCalendar: it = $it")
            Log.d("TAG", "HorizontalCalendar: selectedDate = ${dateFormat.format(selectedDate)}")
            DayItem(
                date = it,
                isSelectedDate = it == dateFormat.format(selectedDate),
                onClick = onClickItem
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        val screenWidth = with(density) {
            configuration.screenWidthDp.dp.toPx() / 2
        }

        lazyListState.scrollToItem(
            index = state.currentDateIndex,
            scrollOffset = -screenWidth.toInt()
        )
    }
}

@Composable
private fun DayItem(
    date: String,
    isSelectedDate: Boolean,
    onClick: (Long) -> Unit,
) {
    val dateFormat = remember { SimpleDateFormat("dd/M/yyyy", Locale.getDefault()) }
    val formatterDayNumber = remember { SimpleDateFormat("dd", Locale.getDefault()) }
    val formatterDayName = remember { SimpleDateFormat("EE", Locale.getDefault()) }

    val dateTime = dateFormat.parse(date)?.time ?: 0

    val dayOfWeek = remember {
        formatterDayName.format(dateTime)
            .replaceFirstChar { it.titlecase(Locale.getDefault()) }
    }

    val dayNumber = formatterDayNumber.format(dateTime)

    val currentDate = Date()
    val sdf = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    val isCurrentDate = sdf.format(currentDate).toInt() == sdf.format(dateTime).toInt()

    val today = System.currentTimeMillis() - 24 * 1000 * 60 * 60L
    val dayNumberColor =
        when {
            isSelectedDate -> MaterialTheme.colorScheme.background
            today <= dateTime -> MaterialTheme.colorScheme.onBackground
            else -> PriorityCardNotAssigned
        }

    val dayNumberModifier = Modifier
        .size(size = 20.dp)
        .clip(shape = CircleShape)
        .let {
            if (isCurrentDate) it.border(
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
                .padding(top = 2.dp)
                .clip(shape = CircleShape)
                .clickableSingle { onClick.invoke(dateTime) }
                .padding(horizontal = 6.dp)
                .padding(top = 2.dp, bottom = 6.dp)
        ) {
            Text(
                text = dayOfWeek,
                style = MaterialTheme.typography.labelMedium,
                color = PriorityNotAssigned,
                modifier = Modifier
                    .padding(top = 2.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = dayNumberModifier
            ) {
                Text(
                    text = dayNumber,
                    style = MaterialTheme.typography.titleSmall,
                    color = dayNumberColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
        HorizontalCalendar(
            selectedDate = Date().time
        ) {}
    }
}

