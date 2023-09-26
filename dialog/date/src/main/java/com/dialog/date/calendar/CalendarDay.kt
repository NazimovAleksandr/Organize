package com.dialog.date.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.ui.modifier.clickableSingle
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
fun RowScope.CalendarDay(
    date: LocalDate,
    colors: CalendarColors,
    dayStyle: TextStyle,
    selectedDayStyle: TextStyle,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
) {
    val selected = selectedDate == date
    val currentDay = Clock.System.todayIn(TimeZone.currentSystemDefault()) == date

    val onClick: () -> Unit = remember { { onDayClick(date) } }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .border(
                border = getBorder(currentDay, colors.todayBorder, selected),
                shape = CircleShape
            )
            .clip(shape = CircleShape)
            .clickableSingle(onClick = onClick, ripple = false)
            .dayBackgroundColor(
                selected = selected,
                color = colors.selectedDayBackground
            )
            .circleLayout()
            .wrapContentSize()
            .aspectRatio(ratio = 1f)
            .weight(weight = 1f, fill = false)
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            textAlign = TextAlign.Center,
            style = if (selected) selectedDayStyle else dayStyle,
            color = if (selected) colors.selectedDayTextColor else colors.dayTextColor,
            modifier = Modifier
        )
    }
}

private fun getBorder(
    currentDay: Boolean,
    color: Color,
    selected: Boolean
): BorderStroke {
    val emptyBorder = BorderStroke(0.dp, Color.Transparent)

    return if (currentDay && selected.not()) {
        BorderStroke(1.dp, color)
    } else {
        emptyBorder
    }
}

@Preview
@Composable
private fun Preview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 100.dp)
    ) {
        CalendarDay(
            date = LocalDate(2023, 7, 1),
            colors = CalendarColors.default(),
            dayStyle = MaterialTheme.typography.titleMedium,
            selectedDayStyle = MaterialTheme.typography.titleMedium,
            selectedDate = LocalDate(2023, 7, 1),
            onDayClick = {}
        )

        CalendarDay(
            date = LocalDate(2023, 7, 2),
            colors = CalendarColors.default(),
            dayStyle = MaterialTheme.typography.titleMedium,
            selectedDayStyle = MaterialTheme.typography.titleMedium,
            selectedDate = LocalDate(2023, 7, 1),
            onDayClick = {}
        )

        CalendarDay(
            date = LocalDate(2023, 9, 6),
            colors = CalendarColors.default(),
            dayStyle = MaterialTheme.typography.titleMedium,
            selectedDayStyle = MaterialTheme.typography.titleMedium,
            selectedDate = LocalDate(2023, 7, 1),
            onDayClick = {}
        )
    }
}

