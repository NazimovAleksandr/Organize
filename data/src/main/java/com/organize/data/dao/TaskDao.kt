package com.organize.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.organize.entity.task.TaskDB
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun set(task: TaskDB)

    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<TaskDB>>

    @Update
    suspend fun update(task: TaskDB)

    @Update
    suspend fun updateAll(list: List<TaskDB>)

    @Delete
    suspend fun delete(task: TaskDB)
}