package com.organize.entity.mapper

import com.organize.entity.task.TaskDB
import com.organize.entity.task.TaskUI
import com.organize.entity.task_category.CategoryDB
import com.organize.entity.task_message.TaskMessage

fun TaskDB.toUI(category: CategoryDB?): TaskUI = TaskUI(
    id = id,
    message = TaskMessage(title = title, description = description),
    date = date,
    priority = priority,
    category = category?.toUI(),
    done = false,
)

fun TaskUI.toDB(): TaskDB = TaskDB(
    id = id,
    title = message.title,
    description = message.description,
    date = date,
    priority = priority,
    categoryId = category?.id,
    done = done
)