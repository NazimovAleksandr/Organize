package com.organize.data

import com.organize.data.dao.CategoryDao
import com.organize.data.dao.HabitDao
import com.organize.data.dao.TaskDao
import com.organize.data.data_base.AppDatabase
import com.organize.entity.habit.HabitUI
import com.organize.entity.mapper.toDB
import com.organize.entity.mapper.toUI
import com.organize.entity.task.TaskUI
import com.organize.entity.task_category.CategoryUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataManager(
    appDatabase: AppDatabase,
) {
    private val taskDao: TaskDao = appDatabase.taskDao()
    private val habitDao: HabitDao = appDatabase.habitDao()
    private val categoryDao: CategoryDao = appDatabase.categoryDao()

    fun getTasks(): Flow<List<TaskUI>> {
        return taskDao.getAll().map { taskDBList ->
            taskDBList.map { taskDB ->
                taskDB.toUI(categoryDao.getById(taskDB.categoryId))
            }
        }
    }

    suspend fun setTask(task: TaskUI) {
        taskDao.set(task.toDB())
    }

    suspend fun updateTask(task: TaskUI) {
        taskDao.update(task.toDB())
    }

    suspend fun updateTasks(tasks: List<TaskUI>) {
        taskDao.updateAll(tasks.map { it.toDB() })
    }

    suspend fun deleteTask(task: TaskUI) {
        taskDao.delete(task.toDB())
    }

    fun getHabits(): Flow<List<HabitUI>> {
        return habitDao.getAll().map { habitDBList ->
            habitDBList.map { habitDB ->
                habitDB.toUI()
            }
        }
    }

    suspend fun setHabit(habit: HabitUI) {
        habitDao.set(habit.toDB())
    }

    suspend fun updateHabit(habit: HabitUI) {
        habitDao.update(habit.toDB())
    }

    suspend fun deleteHabit(habit: HabitUI) {
        habitDao.delete(habit.toDB())
    }

    fun getCategory(): Flow<List<CategoryUI>> {
        return categoryDao.getAll().map { categoryDBList ->
            categoryDBList.map { categoryDB ->
                categoryDB.toUI()
            }
        }
    }

    suspend fun getCategoryById(id: Int): CategoryUI {
        return categoryDao.getById(id).toUI()
    }

    suspend fun setAllCategory(category: List<CategoryUI>) {
        categoryDao.setAll(*category.map { it.toDB() }.toTypedArray())
    }

    suspend fun setCategory(category: CategoryUI) {
        categoryDao.set(category.toDB())
    }

    suspend fun updateCategory(category: CategoryUI) {
        categoryDao.update(category.toDB())
    }

    suspend fun deleteCategory(category: CategoryUI) {
        categoryDao.delete(category.toDB())
    }
}