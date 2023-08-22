package com.screen.tasks

import com.core.mvi.contract.Event
import com.core.mvi.contract.ScreenState
import com.core.mvi.contract.SingleEvent
import com.core.mvi.processor.MviProcessor
import com.organize.entity.Habit
import com.screen.tasks.model.Day
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class TasksState(
    val date: String,
    val habits: List<Habit>,
    val dateRange: List<Day>,
) : ScreenState

sealed class TasksEvent : Event {
    class SelectDate(val date: Long) : TasksEvent()
    data object UpdateHabits : TasksEvent()
}

interface TasksSingleEvent : SingleEvent

class TasksViewModel : MviProcessor<TasksState, TasksEvent, TasksSingleEvent>() {

    private var id = 0

    override fun initialState(): TasksState {
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
            val dayOfWeek = dateFormatDayOfWeek.format(curTime).replaceFirstChar { it.titlecase(Locale.getDefault()) }
            val dayNumber = dateFormatDayNumber.format(curTime) ?: ""

            val dateTime = curTime

            dates.add(
                Day(
                    dayOfWeek = dayOfWeek,
                    number = dayNumber,
                    dateTime = curTime,
                    isSelected = sdf.format(currentDate).toInt() == sdf.format(dateTime).toInt(),
                    isToday = sdf.format(currentDate).toInt() == sdf.format(dateTime).toInt(),
                    isAfterToday = today <= dateTime,
                )
            )

            curTime += interval
        }

        val date = SimpleDateFormat("dd MMMM", Locale.getDefault())

        return TasksState(
            date = date.format(System.currentTimeMillis()) + ", сегодня",
            habits = listOf(
                Habit(
                    id = ++id,
                    icon = com.core.R.drawable.ic_water,
                    progress = 0.3f
                ),
                Habit(
                    id = ++id,
                    icon = com.core.R.drawable.ic_education,
                    progress = 0.6f
                ),
                Habit(
                    id = ++id,
                    icon = com.core.R.drawable.ic_sport,
                    progress = 0.8f
                ),
            ),
            dateRange = dates
        )
    }

    override fun reduce(event: TasksEvent, state: TasksState): TasksState {
        return when (event) {
            is TasksEvent.SelectDate -> {
                val list = state.dateRange.map {
                    when {
                        it.dateTime == event.date && it.isSelected -> it

                        it.isSelected -> it.copy(isSelected = false)
                        it.dateTime == event.date -> it.copy(isSelected = true)

                        else -> it
                    }
                }

                state.copy(dateRange = list)
            }

            is TasksEvent.UpdateHabits -> state.copy(habits = state.habits.toMutableList().let {
                it.add(
                    Habit(
                        id = ++id,
                        icon = com.core.R.drawable.ic_settings,
                        progress = Random.nextDouble(0.0, 1.0).toFloat()
                    )
                )
                it
            })
        }
    }

    override suspend fun handleEvent(event: TasksEvent, state: TasksState): TasksEvent? {
        return null
    }
}