package com.dialog.recall

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.ui.composable.ClearCancelOkButtons
import com.core.ui.composable.Content
import com.core.ui.modifier.advancedShadow
import com.core.ui.modifier.clickableOff
import com.core.ui.modifier.clickableSingle
import com.core.ui.theme.OrganizeTheme
import com.res.R

enum class RecallVariants(@StringRes val value: Int) {
    Saved(0),

    No(R.string.recall_no),
    Time(R.string.recall_time),
    FiveMinutes(R.string.recall_five_minutes),
    FifteenMinutes(R.string.recall_fifteen_minutes),
    ThirtyMinutes(R.string.recall_thirty_minutes),
    OneHour(R.string.recall_one_hour),
    OneDay(R.string.recall_one_day),
}

data class RecallPickerDialogData(
    val selectedVariants: List<RecallVariants>? = null,
)

/**
 * No.
 * In day (time).
 * Before 5 minutes
 * Before 15 minutes
 * Before 30 minutes
 * Before 1 hour
 * Before 1 day
 */
@Composable
fun RecallPickerDialog(
    data: RecallPickerDialogData,
    modifier: Modifier = Modifier,
    onClickClear: () -> Unit,
    onClickCancel: () -> Unit,
    onClickOk: (List<RecallVariants>?) -> Unit,
) {
    Log.d("TAG", "RecallPickerDialog: data = $data")

    var selectedList: List<RecallVariants> by remember(key1 = data) {
        mutableStateOf(data.selectedVariants.getSelectedList(RecallVariants.Saved))
    }

    Log.d("TAG", "RecallPickerDialog: selectedList = $selectedList")

    val variants by remember(key1 = selectedList) {
        mutableStateOf(getVariants(selectedList))
    }

    Content {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(vertical = 52.dp)
                .advancedShadow(
                    alpha = 0.2f,
                    cornersRadius = 28.dp,
                    shadowBlurRadius = 16.dp,
                    offsetY = 4.dp,
                )
                .clip(shape = RoundedCornerShape(size = 28.dp))
                .background(color = MaterialTheme.colorScheme.background)
                .clickableOff()
                .width(intrinsicSize = IntrinsicSize.Min)
        ) {
            Spacer(modifier = Modifier.height(height = 20.dp))

            Text(
                text = stringResource(id = R.string.recall),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 24.dp)
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState())
                    .fillMaxWidth()
                    .width(intrinsicSize = IntrinsicSize.Max)
            ) {
                variants.forEach { (recallVariant, selected) ->
                    RecallVariant(variant = recallVariant, selected = selected) {
                        selectedList = selectedList.getSelectedList(it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(height = 12.dp))

            ClearCancelOkButtons(
                onClickClear = onClickClear,
                onClickCancel = onClickCancel,
                onClickOk = { onClickOk.invoke(selectedList) },
            )
        }
    }
}

@Composable
private fun RecallVariant(
    variant: RecallVariants,
    selected: Boolean,
    select: (RecallVariants) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickableSingle { select.invoke(variant) }
            .fillMaxWidth()
            .width(intrinsicSize = IntrinsicSize.Max)
            .padding(vertical = 8.dp, horizontal = 20.dp)
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .size(size = 24.dp)
                .clip(shape = CircleShape)
                .background(color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary)
                .border(color = MaterialTheme.colorScheme.primary, width = 2.dp, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(width = 16.dp))

        Text(
            text = stringResource(id = variant.value),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
        )
    }
}

private fun List<RecallVariants>?.getSelectedList(
    variant: RecallVariants? = null,
): List<RecallVariants> {
    return when (variant) {
        null -> listOf(RecallVariants.No)
        RecallVariants.No -> listOf(RecallVariants.No)
        RecallVariants.Saved -> this ?: listOf(RecallVariants.No)

        else -> {
            when (this?.contains(RecallVariants.No)) {
                true -> listOf(variant)

                else -> (this?.toMutableList() ?: mutableListOf()).also {
                    when (it.contains(variant)) {
                        true -> it.remove(variant)
                        false -> it.add(variant)
                    }
                }.let {
                    when (it.isEmpty()) {
                        false -> it
                        true -> listOf(RecallVariants.No)
                    }
                }
            }
        }
    }
}

private fun getVariants(list: List<RecallVariants>): Map<RecallVariants, Boolean> {
    return if (list.contains(RecallVariants.No)) {
        mapOf(
            RecallVariants.No to true,
            RecallVariants.Time to false,
            RecallVariants.FiveMinutes to false,
            RecallVariants.FifteenMinutes to false,
            RecallVariants.ThirtyMinutes to false,
            RecallVariants.OneHour to false,
            RecallVariants.OneDay to false,
        )
    } else {
        mapOf(
            RecallVariants.No to false,
            RecallVariants.Time to (list.find { it == RecallVariants.Time } != null),
            RecallVariants.FiveMinutes to (list.find { it == RecallVariants.FiveMinutes } != null),
            RecallVariants.FifteenMinutes to (list.find { it == RecallVariants.FifteenMinutes } != null),
            RecallVariants.ThirtyMinutes to (list.find { it == RecallVariants.ThirtyMinutes } != null),
            RecallVariants.OneHour to (list.find { it == RecallVariants.OneHour } != null),
            RecallVariants.OneDay to (list.find { it == RecallVariants.OneDay } != null),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
        ) {
            RecallPickerDialog(
                data = RecallPickerDialogData(),
                onClickClear = {},
                onClickCancel = {},
                onClickOk = {},
            )
        }
    }
}