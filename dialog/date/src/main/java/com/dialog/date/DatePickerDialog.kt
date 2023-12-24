package com.dialog.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.core.ui.composable.ClearCancelOkButtons
import com.core.ui.composable.Content
import com.core.ui.modifier.clickableOff
import com.core.ui.modifier.clickableSingle
import com.dialog.date.calendar.Calendar
import com.dialog.date.calendar.CalendarType
import com.res.R
import java.time.LocalDate

sealed interface DatePickerDialogCommand {
    object TimeStart : DatePickerDialogCommand
    object TimeEnd : DatePickerDialogCommand
    object Recall : DatePickerDialogCommand
    object Repeat : DatePickerDialogCommand

    object Clear : DatePickerDialogCommand

    object Cancel : DatePickerDialogCommand
    class Ok(val date: LocalDate) : DatePickerDialogCommand
    class Select(val date: LocalDate) : DatePickerDialogCommand
}

data class DatePickerDialogData(
    val currentDay: LocalDate = LocalDate.now(),
    val timeStart: String,
    val timeEnd: String,
    val recall: String? = null,
    val repeat: String = "",
)

@Composable
fun DatePickerDialog(
    data: DatePickerDialogData,
    onClick: (DatePickerDialogCommand) -> Unit,
) {
    DatePicker(data, onClick)
}

@Composable
private fun DatePicker(
    data: DatePickerDialogData,
    onClick: (DatePickerDialogCommand) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectDate by remember { mutableStateOf(data.currentDay) }
    val context = LocalContext.current

    Content {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .padding(horizontal = 24.dp)
                .clickableOff()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(size = 28.dp)
                )
                .padding(top = 4.dp)
        ) {
            Calendar(
                currentDay = data.currentDay,
                calendarType = CalendarType.Month,
                modifier = Modifier,
                onDayClick = {
                    selectDate = it
                    onClick.invoke(DatePickerDialogCommand.Select(date = selectDate))
                }
            )

            Spacer(modifier = Modifier.height(height = 12.dp))

            SettingsItem(
                icon = R.drawable.ic_time_start,
                text = R.string.time_start,
                data = data.timeStart,
                onClick = { onClick.invoke(DatePickerDialogCommand.TimeStart) }
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            SettingsItem(
                icon = R.drawable.ic_time_end,
                text = R.string.time_end,
                data = data.timeEnd,
                onClick = { onClick.invoke(DatePickerDialogCommand.TimeEnd) }
            )

            Spacer(modifier = Modifier.height(height = 8.dp))

            SettingsItem(
                icon = R.drawable.ic_bell,
                text = R.string.recall,
                data = data.recall ?: context.getString(R.string.recall_no),
                onClick = { onClick.invoke(DatePickerDialogCommand.Recall) }
            )

            /*Spacer(modifier = Modifier.height(height = 8.dp))

            SettingsItem(
                icon = R.drawable.ic_refresh,
                text = R.string.repeat,
                data = data.repeat,
                onClick = { onClick.invoke(DatePickerDialogCommand.Repeat) }
            )*/

            Spacer(modifier = Modifier.height(height = 12.dp))

            ClearCancelOkButtons(
                onClickClear = { onClick.invoke(DatePickerDialogCommand.Clear) },
                onClickCancel = { onClick.invoke(DatePickerDialogCommand.Cancel) },
                onClickOk = { onClick.invoke(DatePickerDialogCommand.Ok(date = selectDate)) },
            )
        }
    }
}

@Composable
private fun SettingsItem(
    icon: Int,
    text: Int,
    data: String,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickableSingle(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(size = 24.dp)
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.weight(weight = 1f))

        Text(
            text = data,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(end = 8.dp, start = 50.dp)
        )
    }
}