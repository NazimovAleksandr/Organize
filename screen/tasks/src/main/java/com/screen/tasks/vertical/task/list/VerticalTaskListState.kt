package com.screen.tasks.vertical.task.list

import com.organize.entity.task.TaskUI
import kotlinx.coroutines.flow.Flow

data class VerticalTaskListState(
    val tasks: Flow<Map<String, List<TaskUI>>>,
    val tasksType: VerticalTaskListType,
    val selectedDate: Long,
)
