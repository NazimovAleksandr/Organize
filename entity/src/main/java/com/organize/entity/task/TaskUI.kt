package com.organize.entity.task

import com.organize.entity.task_category.CategoryUI
import com.organize.entity.task_date.TaskDate
import com.organize.entity.task_message.TaskMessage
import com.organize.entity.task_priority.TaskPriority

data class TaskUI(
    val id: Int,
    val message: TaskMessage,
    val date: TaskDate,
    val priority: TaskPriority,
    val category: CategoryUI?,
    val done: Boolean,
)
