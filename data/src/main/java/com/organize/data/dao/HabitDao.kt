package com.organize.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.organize.entity.habit.HabitDB
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Insert
    suspend fun set(habit: HabitDB)

    @Query("SELECT * FROM habits")
    fun getAll(): Flow<List<HabitDB>>

    @Update
    suspend fun update(habit: HabitDB)

    @Delete
    suspend fun delete(habit: HabitDB)
}