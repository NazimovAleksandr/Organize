package com.dialog.category

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.ui.composable.Content
import com.core.ui.modifier.advancedShadow
import com.core.ui.modifier.clickableOff
import com.core.ui.modifier.clickableSingle
import com.organize.entity.task_category.CategoryUI
import com.res.R
import kotlinx.coroutines.flow.Flow

@Composable
fun CategoryPickerDialog(
    list: Flow<List<CategoryUI>>,
    add: () -> Unit,
    select: (CategoryUI) -> Unit,
) {
    val items by list.collectAsStateWithLifecycle(initialValue = emptyList())

    Content {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
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
                .padding(all = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.add_to_category),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(height = 20.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState())
                    .fillMaxWidth()
                    .width(intrinsicSize = IntrinsicSize.Max)
            ) {
                items.forEach {
                    CategoryItem(item = it) {
                        select.invoke(it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(height = 22.dp))

            Text(
                text = stringResource(id = R.string.add_new_category),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(intrinsicSize = IntrinsicSize.Max)
                    .clip(shape = RoundedCornerShape(size = 6.dp))
                    .clickableSingle(onClick = add)
            )
        }
    }
}

@Composable
private fun CategoryItem(
    item: CategoryUI,
    select: () -> Unit,
) {
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
            imageVector = ImageVector.vectorResource(id = item.icon.value),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
        )
    }
}