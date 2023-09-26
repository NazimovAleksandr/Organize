package com.organize.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.organize.entity.task_category.CategoryDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun set(group: CategoryDB)

    @Insert
    suspend fun setAll(vararg group: CategoryDB)

    @Query("SELECT * FROM category")
    fun getAll(): Flow<List<CategoryDB>>

    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int?): CategoryDB

    @Update
    suspend fun update(group: CategoryDB)

    @Delete
    suspend fun delete(group: CategoryDB)
}