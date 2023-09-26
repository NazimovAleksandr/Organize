package com.screen.tasks

import androidx.lifecycle.viewModelScope
import com.core.mvi.contract.ScreenEvent
import com.core.mvi.contract.ScreenState
import com.core.mvi.contract.ScreenSingleEvent
import com.core.mvi.processor.MviProcessor
import com.dialog.newtask.TaskData
import com.organize.data.DataManager
import com.organize.entity.habit.HabitUI
import com.organize.entity.task_priority.TaskPriority
import com.organize.entity.task.TaskUI
import com.organize.entity.task_category.CategoryUI
import com.organize.entity.task_date.TaskDate
import com.organize.entity.task_message.TaskMessage
import com.screen.tasks.vertical.task.list.VerticalTaskListState
import com.screen.tasks.vertical.task.list.VerticalTaskListType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class TasksScreenState(
    val date: String,
    val habits: Flow<List<HabitUI>>,
    val taskListState: VerticalTaskListState,
) : ScreenState

sealed class TasksScreenEvent : ScreenEvent {
    class SelectDate(val date: Long) : TasksScreenEvent()

    object AddTask : TasksScreenEvent()

    class CreateTask(
        val taskData: TaskData,
    ) : TasksScreenEvent()
}

enum class TasksScreenSingleEvent : ScreenSingleEvent {
    NewTask
}

class TasksViewModel(
    private val dataManager: DataManager,
) : MviProcessor<TasksScreenState, TasksScreenEvent, TasksScreenSingleEvent>() {

    private val habits: Flow<List<HabitUI>> = dataManager.getHabits()
    private val tasks: Flow<List<TaskUI>> = dataManager.getTasks()
    private val category: Flow<List<CategoryUI>> = dataManager.getCategory()

    override fun initialState(): TasksScreenState {
        val date = SimpleDateFormat("dd MMMM", Locale.getDefault())

        updateUndoneTasks(tasks)

        return TasksScreenState(
            date = date.format(System.currentTimeMillis()),
            habits = habits,
            taskListState = VerticalTaskListState(
                tasks = tasks.map { taskUIList ->
                    val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())

                    taskUIList.groupBy { taskUI ->
                        dateFormat.format(taskUI.date.date)
                    }
                },
                tasksType = VerticalTaskListType.OneDay,
                selectedDate = System.currentTimeMillis()
            ),
        )
    }

    override fun reduce(event: TasksScreenEvent, state: TasksScreenState): TasksScreenState {
        return when (event) {
            is TasksScreenEvent.SelectDate -> {
                state.copy(
                    taskListState = state.taskListState.copy(selectedDate = event.date)
                )
            }

            is TasksScreenEvent.AddTask -> state
            is TasksScreenEvent.CreateTask -> state
        }
    }

    override suspend fun handleEvent(event: TasksScreenEvent, state: TasksScreenState): TasksScreenEvent? {
        when (event) {
            is TasksScreenEvent.AddTask -> {
                triggerSingleEvent(singleEvent = TasksScreenSingleEvent.NewTask)
            }

            is TasksScreenEvent.CreateTask -> {
                createTask(
                    message = event.taskData.message,
                    date = event.taskData.date,
                    priority = event.taskData.priority,
                    categoryId = event.taskData.category?.id,
                )
            }

            is TasksScreenEvent.SelectDate -> {}
        }

        return null
    }

    private fun updateUndoneTasks(tasks: Flow<List<TaskUI>>) {
        viewModelScope.launch(Dispatchers.IO) {
            tasks.first()
                .filter { !it.done }
                .filter { it.date.date < System.currentTimeMillis() }
                .map { it.copy(date = it.date.copy(date = System.currentTimeMillis())) }
                .also { dataManager.updateTasks(it) }

        }
    }

    private fun createTask(
        message: TaskMessage,
        date: TaskDate,
        priority: TaskPriority?,
        categoryId: Int?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = tasks.firstOrNull()?.maxOfOrNull { it.id } ?: 0
            val categoryUI = category.first().find { it.id == categoryId }

            dataManager.setTask(
                TaskUI(
                    id = id + 1,
                    message = message,
                    date = date,
                    priority = priority ?: TaskPriority.No,
                    category = categoryUI,
                    done = false,
                )
            )
        }
    }
}