package com.core.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.res.R

@Composable
fun ClearCancelOkButtons(
    onClickClear:() -> Unit,
    onClickCancel:() -> Unit,
    onClickOk:() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(bottom = 9.dp, top = 8.dp)
    ) {
        TextButton(
            onClick = onClickClear
        ) {
            Text(
                text = stringResource(id = R.string.clear),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.weight(weight = 1f))

        TextButton(
            onClick = onClickCancel
        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.width(width = 4.dp))

        TextButton(
            onClick = onClickOk
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