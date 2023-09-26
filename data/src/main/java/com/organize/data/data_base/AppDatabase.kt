package com.organize.data.data_base

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.organize.data.converter.TaskDateConverter
import com.organize.data.dao.CategoryDao
import com.organize.data.dao.HabitDao
import com.organize.data.dao.TaskDao
import com.organize.entity.habit.HabitDB
import com.organize.entity.task.TaskDB
import com.organize.entity.task_category.CategoryDB

@Database(
    entities = [TaskDB::class, HabitDB::class, CategoryDB::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(value = [TaskDateConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao
    abstract fun categoryDao(): CategoryDao
}