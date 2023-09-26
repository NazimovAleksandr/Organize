package com.organize.entity.mapper

import com.organize.entity.task_category.CategoryDB
import com.organize.entity.task_category.CategoryUI

fun CategoryDB.toUI(): CategoryUI = CategoryUI(
    id = id,
    icon = icon,
    name = name,
)

fun CategoryUI.toDB(): CategoryDB = CategoryDB(
    id = id,
    icon = icon,
    name = name,
)