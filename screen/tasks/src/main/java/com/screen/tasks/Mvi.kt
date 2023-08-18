package com.screen.tasks

import com.core.mvi.contract.Event
import com.core.mvi.contract.ScreenState
import com.core.mvi.contract.SingleEvent
import com.core.mvi.processor.MviProcessor
import com.organize.entity.Habit
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

data class TasksState(
    val date: String,
    val habits: List<Habit>,
    val selectedDate: Long,
) : ScreenState

sealed class TasksEvent : Event {
    class SelectDate(val date: Long) : TasksEvent()
    data object UpdateHabits : TasksEvent()
}

interface TasksSingleEvent : SingleEvent

class TasksViewModel : MviProcessor<TasksState, TasksEvent, TasksSingleEvent>() {

    private var id = 0

    override fun initialState(): TasksState {
        val date = SimpleDateFormat("dd MMMM", Locale.getDefault())

        val dateFormat = SimpleDateFormat("dd/M/yyyy", Locale.getDefault())
        val todayDate = dateFormat.format(System.currentTimeMillis())
        val curTime = dateFormat.parse(todayDate)?.time ?: 0L

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
            selectedDate = curTime
        )
    }

    override fun reduce(event: TasksEvent, state: TasksState): TasksState {
        return when (event) {
            is TasksEvent.SelectDate -> state.copy(selectedDate = event.date)

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