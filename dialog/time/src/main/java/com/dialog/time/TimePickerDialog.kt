package com.dialog.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.ui.modifier.clickableOff
import com.core.ui.theme.OrganizeTheme
import com.res.R

data class TimePickerDialogData(
    val initialHour: Int,
    val initialMinute: Int,
)

private enum class TimePickerType {
    Dial, Input
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    data: TimePickerDialogData,
    onDismiss: (Int?, Int?) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = data.initialHour, initialMinute = data.initialMinute,
    )

    var timePickerType by remember {
        mutableStateOf(TimePickerType.Dial)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(intrinsicSize = IntrinsicSize.Max)
            .clickableOff()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(size = 28.dp)
            )
            .padding(horizontal = 12.dp)
            .padding(top = 24.dp)
    ) {
        when (timePickerType) {
            TimePickerType.Dial -> TimePickerDial(
                timePickerState = timePickerState,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 16.dp)
            )

            TimePickerType.Input -> TimePickerInput(
                timePickerState = timePickerState,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(bottom = 9.dp, top = 8.dp)
        ) {
            IconButton(
                onClick = {
                    timePickerType = when (timePickerType) {
                        TimePickerType.Dial -> TimePickerType.Input
                        TimePickerType.Input -> TimePickerType.Dial
                    }
                }
            ) {
                when (timePickerType) {
                    TimePickerType.Dial -> Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_keyboard),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )

                    TimePickerType.Input -> Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_time_start),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

            TextButton(
                onClick = { onDismiss.invoke(null, null) }
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.width(width = 8.dp))

            TextButton(
                onClick = {
                    onDismiss.invoke(
                        timePickerState.hour,
                        timePickerState.minute,
                    )
                }
            ) {
                Text(
                    text = stringResource(id = R.string.ok),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDial(
    timePickerState: TimePickerState,
    modifier: Modifier = Modifier,
) {
    TimePicker(
        state = timePickerState,
        colors = TimePickerDefaults.colors(
            clockDialColor = MaterialTheme.colorScheme.scrim,
            clockDialSelectedContentColor = MaterialTheme.colorScheme.onBackground,
            clockDialUnselectedContentColor = MaterialTheme.colorScheme.onBackground,

            selectorColor = MaterialTheme.colorScheme.outlineVariant,

            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.outlineVariant,
            timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.scrim,
            timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onBackground,
            timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onBackground,

            periodSelectorBorderColor = Color.Transparent,
            periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.outlineVariant,
            periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.scrim,
            periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onBackground,
            periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerInput(
    timePickerState: TimePickerState,
    modifier: Modifier = Modifier,
) {
    TimeInput(
        state = timePickerState,
        colors = TimePickerDefaults.colors(
            periodSelectorBorderColor = Color.Transparent,
            periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.outlineVariant,
            periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.scrim,
            periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onBackground,
            periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onBackground,

            timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.outlineVariant,
            timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.scrim,
            timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onBackground,
            timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            TimePickerDial(timePickerState = rememberTimePickerState(10, 0, true))
            TimePickerInput(timePickerState = rememberTimePickerState(10, 0, true))
        }
    }
}