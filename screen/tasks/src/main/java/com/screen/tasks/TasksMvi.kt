package com.screen.tasks

import androidx.lifecycle.viewModelScope
import com.core.mvi.contract.ScreenEvent
import com.core.mvi.contract.ScreenSingleEvent
import com.core.mvi.contract.ScreenState
import com.core.mvi.processor.MviProcessor
import com.dialog.newtask.TaskData
import com.organize.data.DataManager
import com.organize.entity.habit.HabitUI
import com.organize.entity.task.TaskUI
import com.organize.entity.task_category.CategoryUI
import com.organize.entity.task_date.TaskDate
import com.organize.entity.task_message.TaskMessage
import com.organize.entity.task_priority.TaskPriority
import com.screen.tasks.horizontal.calendar.Day
import com.screen.tasks.vertical.task.list.VerticalTaskListState
import com.screen.tasks.vertical.task.list.VerticalTaskListType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class TasksScreenState(
    val day: String,
    val month: String,
    val year: String,
    val habits: Flow<List<HabitUI>>,
    val dateRange: List<Day>,
    val selectedDay: Long,
    val animateScroll: Long = 0,
    val taskListState: VerticalTaskListState,
) : ScreenState

sealed class TasksScreenEvent : ScreenEvent {
    object SelectTodayDate : TasksScreenEvent()
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

    private val day = SimpleDateFormat("dd", Locale.getDefault())
    private val month = SimpleDateFormat("MMMM", Locale.getDefault())
    private val year = SimpleDateFormat("yyyy", Locale.getDefault())

    override fun initialState(): TasksScreenState {
        updateUndoneTasks(tasks)

        val time = System.currentTimeMillis()
        val dateRange = getDatesRange()

        return TasksScreenState(
            day = day.format(time),
            month = month.format(time),
            year = year.format(time),
            habits = habits,
            dateRange = dateRange,
            selectedDay = dateRange.first { it.isToday }.dateTime,
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
            is TasksScreenEvent.SelectTodayDate -> {
                val time = System.currentTimeMillis()

                state.copy(
                    day = day.format(time),
                    month = month.format(time),
                    year = year.format(time),
                    selectedDay = state.dateRange.first { it.isToday }.dateTime,
                    animateScroll = state.animateScroll + 1,
                    taskListState = state.taskListState.copy(selectedDate = System.currentTimeMillis())
                )
            }

            is TasksScreenEvent.SelectDate -> {
                state.copy(
                    day = day.format(event.date),
                    month = month.format(event.date),
                    year = year.format(event.date),
                    selectedDay = event.date,
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

            is TasksScreenEvent.SelectTodayDate -> {}

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

    private fun getDatesRange(): List<Day> {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())

        val startDateString =
            "${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR] - 1}"
        val startDate = dateFormat.parse(startDateString)

        val endDateString =
            "${calendar[Calendar.DAY_OF_MONTH]}/${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR] + 1}"
        val endDate = dateFormat.parse(endDateString)

        val dates: MutableList<Day> = mutableListOf()
        var curTime = startDate?.time ?: 0L
        val endTime = endDate?.time ?: 0L
        val interval = 24 * 1000 * 60 * 60L

        val dateFormatDayOfWeek = SimpleDateFormat("EE", Locale.getDefault())
        val dateFormatDayNumber = SimpleDateFormat("dd", Locale.getDefault())

        val today = System.currentTimeMillis() - 24 * 1000 * 60 * 60L

        val currentDate = Date()
        val sdf = SimpleDateFormat("ddMMyyyy", Locale.getDefault())

        while (curTime <= endTime) {
            val dayOfWeek = dateFormatDayOfWeek.format(curTime).replaceFirstChar { it.titlecaseChar() }
            val dayNumber = dateFormatDayNumber.format(curTime) ?: ""

            val dateTime = curTime

            dates.add(
                Day(
                    dayOfWeek = dayOfWeek,
                    number = dayNumber,
                    dateTime = curTime,
                    isToday = sdf.format(currentDate).toInt() == sdf.format(dateTime).toInt(),
                    isAfterToday = today <= dateTime,
                )
            )

            curTime += interval
        }

        return dates
    }
}