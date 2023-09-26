package com.dialog.date.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.core.ui.theme.OrganizeTheme
import kotlinx.datetime.Month
import java.util.Locale

@Composable
fun CalendarHeader(
    month: Month,
    year: Int,
    textStyle: TextStyle,
    colors: CalendarColors,
    modifier: Modifier = Modifier,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    arrowShown: Boolean = true,
) {
    var isNext by remember { mutableStateOf(true) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {

        if (arrowShown) {
            CalendarIconButton(
                imageVector = Icons.Default.KeyboardArrowLeft,
                color = colors.headerTextColor,
                contentDescription = "Previous Month",
                onClick = {
                    isNext = false
                    onPreviousClick()
                },
                modifier = Modifier
                    .wrapContentSize()
            )
        }

        val titleText = remember(month, year) {
            getTitleText(month, year)
        }

        Text(
            text = titleText,
            style = textStyle,
            color = colors.headerTextColor,
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .align(Alignment.CenterVertically)
        )

        if (arrowShown) {
            CalendarIconButton(
                imageVector = Icons.Default.KeyboardArrowRight,
                color = colors.headerTextColor,
                contentDescription = "Next Month",
                onClick = {
                    isNext = true
                    onNextClick()
                },
                modifier = Modifier
                    .wrapContentSize()
            )
        }
    }
}

internal fun getTitleText(month: Month, year: Int): String {
    val monthDisplayName = month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        .lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    val shortYear = year.toString().takeLast(4)

    return "$monthDisplayName $shortYear"
}

@MultiplePreviews
@Composable
private fun CalendarHeaderPreview() {
    OrganizeTheme {
        CalendarHeader(
            month = java.time.Month.APRIL,
            colors = CalendarColors.default(),
            year = 2023,
            textStyle = MaterialTheme.typography.titleSmall
        )
    }
}
