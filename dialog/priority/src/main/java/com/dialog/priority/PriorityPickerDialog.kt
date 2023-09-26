package com.dialog.priority

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.ui.composable.Content
import com.core.ui.modifier.advancedShadow
import com.core.ui.modifier.clickableOff
import com.core.ui.modifier.clickableSingle
import com.core.ui.theme.PriorityHigh
import com.core.ui.theme.PriorityLow
import com.core.ui.theme.PriorityMedium
import com.core.ui.theme.PriorityNotAssigned
import com.organize.entity.task_priority.TaskPriority
import com.res.R

@Composable
fun PriorityPickerDialog(select: (TaskPriority) -> Unit) {
    Content {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(bottom = 52.dp)
                .advancedShadow(
                    alpha = 0.2f,
                    cornersRadius = 28.dp,
                    shadowBlurRadius = 16.dp,
                    offsetY = 4.dp,
                )
                .clip(shape = RoundedCornerShape(size = 28.dp))
                .background(color = MaterialTheme.colorScheme.background)
                .clickableOff()
                .padding(all = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.priority),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(height = 20.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .width(intrinsicSize = IntrinsicSize.Min)
            ) {
                PriorityItem(priority = TaskPriority.High, select = { select.invoke(TaskPriority.High) })
                PriorityItem(priority = TaskPriority.Medium, select = { select.invoke(TaskPriority.Medium) })
                PriorityItem(priority = TaskPriority.Low, select = { select.invoke(TaskPriority.Low) })
                PriorityItem(priority = TaskPriority.No, select = { select.invoke(TaskPriority.No) })
            }
        }
    }
}

@Composable
private fun PriorityItem(
    priority: TaskPriority,
    select: () -> Unit,
) {
    val text = when (priority) {
        TaskPriority.High -> R.string.priority_high
        TaskPriority.Medium -> R.string.priority_medium
        TaskPriority.Low -> R.string.priority_low
        TaskPriority.No -> R.string.priority_no
    }

    val tint = when (priority) {
        TaskPriority.High -> PriorityHigh
        TaskPriority.Medium -> PriorityMedium
        TaskPriority.Low -> PriorityLow
        TaskPriority.No -> PriorityNotAssigned
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .clip(shape = RoundedCornerShape(size = 6.dp))
            .clickableSingle(onClick = select)
            .fillMaxWidth()
            .width(intrinsicSize = IntrinsicSize.Max)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_priority_task_full),
            contentDescription = null,
            tint = tint,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            maxLines = 1,
            modifier = Modifier
        )
    }
}