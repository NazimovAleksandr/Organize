package com.organize.entity.mapper

import com.organize.entity.habit.HabitDB
import com.organize.entity.habit.HabitUI
import com.organize.entity.icons.Icons

fun HabitDB.toUI(): HabitUI = HabitUI(
    id = id,
    icon = Icons.entries.find { it.name == icon } ?: Icons.Non,
    progress = progress,
)

fun HabitUI.toDB(): HabitDB = HabitDB(
    id = id,
    icon = icon.name,
    progress = progress,
)