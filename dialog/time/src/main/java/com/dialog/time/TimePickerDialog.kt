package com.dialog.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.core.ui.modifier.clickableOff
import com.res.R

data class TimePickerDialogData(
    val initialHour: Int,
    val initialMinute: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    data: TimePickerDialogData,
    onDismiss: (Int?, Int?) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = data.initialHour, initialMinute = data.initialMinute,
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickableOff()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(size = 28.dp)
            )
            .padding(top = 60.dp)
    ) {
        TimePicker(
            state = timePickerState,
            colors = TimePickerDefaults.colors(
                clockDialColor = MaterialTheme.colorScheme.scrim,
                clockDialSelectedContentColor = MaterialTheme.colorScheme.onBackground,
                clockDialUnselectedContentColor = MaterialTheme.colorScheme.onBackground,
                selectorColor = MaterialTheme.colorScheme.outlineVariant,
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
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 9.dp, top = 8.dp)
        ) {
            /*IconButton(
                onClick = {
                    // todo
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_keyboard),
                    contentDescription = null,
                )
            }*/

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