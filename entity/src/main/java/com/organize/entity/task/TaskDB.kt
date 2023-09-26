package com.organize.entity.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.organize.entity.task_date.TaskDate
import com.organize.entity.task_priority.TaskPriority

@Entity(tableName = "tasks")
class TaskDB(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val date: TaskDate,
    val priority: TaskPriority,
    val categoryId: Int?,
    val done: Boolean,
)
