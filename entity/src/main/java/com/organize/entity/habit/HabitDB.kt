package com.organize.entity.habit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
class HabitDB(
    @PrimaryKey
    val id: Int,
    val icon: String,
    val progress: Float,
)