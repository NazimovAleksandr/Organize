package com.screen.tasks.vertical.task.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.ui.composable.Content
import com.organize.entity.task.TaskUI
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun Tasks(
    tasksFlow: Flow<Map<String, List<TaskUI>>>,
    tasksType: VerticalTaskListType,
    selectedDate: Long,
    height: Dp,
) {
    val tasks by tasksFlow.collectAsStateWithLifecycle(initialValue = emptyMap())

    when (tasksType) {
        VerticalTaskListType.OneDay -> {
            val sdf = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())

            val todayDate = sdf.format(Date())

            val list = tasks.filter {
                it.key == sdf.format(selectedDate)
            }.values.flatten()

            val isToday = sdf.format(selectedDate) == todayDate

            Content {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 5.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    TasksTheDay(
                        tasksTheDay = list,
                        height = height,
                        isToday = isToday
                    )
                }
            }
        }

        VerticalTaskListType.ThreeDays -> {}

        VerticalTaskListType.SevenDays -> {}
    }
}

@Composable
private fun TasksTheDay(
    tasksTheDay: List<TaskUI>,
    height: Dp,
    isToday: Boolean,
) {
    Content {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxSize()
        ) {
            TaskLayout(
                list = tasksTheDay,
                modifier = Modifier
                    .height(height = height)
                    .padding(horizontal = 5.dp)
            )

            if (isToday) {
                TimeLine(height = height)
            }
        }
    }
}
