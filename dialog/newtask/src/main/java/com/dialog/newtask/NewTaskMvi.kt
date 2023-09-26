package com.dialog.newtask

import android.content.Context
import com.core.mvi.contract.ScreenEvent
import com.core.mvi.contract.ScreenSingleEvent
import com.core.mvi.contract.ScreenState
import com.core.mvi.processor.MviProcessor
import com.dialog.date.DatePickerDialogData
import com.dialog.time.TimePickerDialogData
import com.organize.data.DataManager
import com.organize.entity.task_category.CategoryUI
import com.organize.entity.task_date.TaskDate
import com.organize.entity.task_message.TaskMessage
import com.organize.entity.task_priority.TaskPriority
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class DialogType {
    Back, Base, DatePicker, TimePickerStart, TimePickerEnd, Priority, Category, FullSize
}

data class TaskData(
    val message: TaskMessage,
    val date: TaskDate,
    val priority: TaskPriority? = null,
    val category: CategoryUI? = null,
)

data class NewTaskDialogData(
    val title: String,
    val description: String,
    val descriptionDate: String,
    val negativeDate: Boolean = false,
    val priority: TaskPriority? = null,
    val category: CategoryUI? = null,
)

data class NewTaskState(
    val dialogType: DialogType,
    val newTaskDialogData: NewTaskDialogData,
    val datePickerDialogData: DatePickerDialogData,
    val timeStartPickerDialogData: TimePickerDialogData,
    val timeEndPickerDialogData: TimePickerDialogData,
    val categoryList: Flow<List<CategoryUI>>,
) : ScreenState

sealed interface NewTaskEvent : ScreenEvent {
    class LoadDate(val context: Context) : NewTaskEvent
    object OnBack : NewTaskEvent

    class Title(val title: String) : NewTaskEvent
    class Description(val description: String) : NewTaskEvent
    object OpenFullSize : NewTaskEvent
    object OpenDatePicker : NewTaskEvent
    object OpenTimeStartPicker : NewTaskEvent
    object OpenTimeEndPicker : NewTaskEvent
    object OpenPriority : NewTaskEvent
    object OpenCategory : NewTaskEvent
    object SaveTask : NewTaskEvent

    class Category(val category: CategoryUI?) : NewTaskEvent
    class Priority(val priority: TaskPriority?) : NewTaskEvent

    class SelectDate(val context: Context, val date: LocalDate) : NewTaskEvent
    class ClearDate(val context: Context) : NewTaskEvent
    class TempDate(val date: LocalDate) : NewTaskEvent
    object CancelDate : NewTaskEvent

    class TimeStart(val hour: Int?, val minute: Int?) : NewTaskEvent
    class TimeEnd(val hour: Int?, val minute: Int?) : NewTaskEvent
}

sealed interface NewTaskSingleEvent : ScreenSingleEvent {
    class Save(val taskData: TaskData) : NewTaskSingleEvent
}

class NewTaskViewModel(dataManager: DataManager) : MviProcessor<NewTaskState, NewTaskEvent, NewTaskSingleEvent>() {

    private val categories: Flow<List<CategoryUI>> = dataManager.getCategory()

    private var title: String = ""
    private var description: String = ""
    private var descriptionDate: String = ""
    private var priority: TaskPriority? = null
    private var category: CategoryUI? = null

    private var date: LocalDate = LocalDate.now()
    private var timeStart: Int? = null
    private var timeEnd: Int? = null

    private var tempDate: LocalDate = LocalDate.now()
    private var tempTimeStart: Int? = null
    private var tempTimeEnd: Int? = null

    override fun initialState(): NewTaskState {
        return NewTaskState(
            dialogType = DialogType.Base,
            newTaskDialogData = NewTaskDialogData(
                title = title,
                description = description,
                descriptionDate = descriptionDate,
            ),
            datePickerDialogData = DatePickerDialogData(),
            timeStartPickerDialogData = getTimePickerDialogData(time = timeStart),
            timeEndPickerDialogData = getTimePickerDialogData(time = timeEnd),
            categoryList = categories,
        )
    }

    override fun reduce(event: NewTaskEvent, state: NewTaskState): NewTaskState {
        return when (event) {
            is NewTaskEvent.LoadDate -> state.copy(
                newTaskDialogData = state.newTaskDialogData.copy(descriptionDate = date.convertDate(event.context))
            )

            is NewTaskEvent.OnBack -> {
                when (state.dialogType) {
                    DialogType.DatePicker -> state.copy(
                        dialogType = DialogType.Base,
                        newTaskDialogData = state.newTaskDialogData.copy(title = title, description = description)
                    )

                    DialogType.TimePickerStart -> state.copy(dialogType = DialogType.DatePicker)
                    DialogType.TimePickerEnd -> state.copy(dialogType = DialogType.DatePicker)

                    else -> state.copy(dialogType = DialogType.Back)
                }
            }

            is NewTaskEvent.OpenFullSize -> state.copy(dialogType = DialogType.FullSize)
            is NewTaskEvent.OpenDatePicker -> state.copy(dialogType = DialogType.DatePicker)
            is NewTaskEvent.OpenTimeStartPicker -> state.copy(dialogType = DialogType.TimePickerStart)
            is NewTaskEvent.OpenTimeEndPicker -> state.copy(dialogType = DialogType.TimePickerEnd)
            is NewTaskEvent.OpenPriority -> state.copy(dialogType = DialogType.Priority)
            is NewTaskEvent.OpenCategory -> state.copy(dialogType = DialogType.Category)

            is NewTaskEvent.Title -> state.apply { title = event.title }
            is NewTaskEvent.Description -> state.apply { description = event.description }

            is NewTaskEvent.Priority -> state.copy(
                dialogType = DialogType.Base,
                newTaskDialogData = state.newTaskDialogData.copy(priority = event.priority)
            ).apply { priority = event.priority }

            is NewTaskEvent.Category -> state.copy(
                dialogType = DialogType.Base,
                newTaskDialogData = state.newTaskDialogData.copy(category = event.category)
            ).apply { category = event.category }

            is NewTaskEvent.SaveTask -> state

            is NewTaskEvent.ClearDate -> {
                tempTimeStart = null
                tempTimeEnd = null
                timeStart = null
                timeEnd = null
                date = LocalDate.now()
                tempDate = LocalDate.now()

                state.copy(
                    newTaskDialogData = state.newTaskDialogData.copy(
                        negativeDate = false,
                        descriptionDate = date.convertDate(event.context),
                    ),
                    datePickerDialogData = DatePickerDialogData(),
                    timeStartPickerDialogData = getTimePickerDialogData(time = tempTimeStart),
                    timeEndPickerDialogData = getTimePickerDialogData(time = tempTimeEnd),
                )
            }

            is NewTaskEvent.CancelDate -> {
                state.copy(
                    dialogType = DialogType.Base,
                    newTaskDialogData = state.newTaskDialogData.copy(
                        title = title,
                        description = description,
                    ),
                    datePickerDialogData = state.datePickerDialogData.copy(
                        currentDay = date,
                        timeStart = getCorrectTime(time = timeStart),
                        timeEnd = getCorrectTime(time = timeEnd),
                    ),
                    timeStartPickerDialogData = getTimePickerDialogData(time = timeStart),
                    timeEndPickerDialogData = getTimePickerDialogData(time = timeEnd),
                )
            }

            is NewTaskEvent.SelectDate -> {
                date = event.date

                timeStart = tempTimeStart
                timeEnd = tempTimeEnd

                state.copy(
                    dialogType = DialogType.Base,
                    newTaskDialogData = state.newTaskDialogData.copy(
                        title = title,
                        description = description,
                        negativeDate = date < LocalDate.now(),
                        descriptionDate = date.convertDate(event.context),
                    ),
                    datePickerDialogData = state.datePickerDialogData.copy(currentDay = date)
                )
            }

            is NewTaskEvent.TempDate -> {
                tempDate = event.date

                state.copy(
                    datePickerDialogData = state.datePickerDialogData.copy(currentDay = tempDate)
                )
            }

            is NewTaskEvent.TimeStart -> {
                tempTimeStart = getCorrectTime(hour = event.hour, minute = event.minute) ?: tempTimeStart

                if (tempTimeStart != null && tempTimeEnd == null) {
                    tempTimeEnd = getCorrectTime(hour = event.hour, minute = (event.minute ?: 0) + 30)
                }

                updateTime()

                state.copy(
                    dialogType = DialogType.DatePicker,
                    datePickerDialogData = state.datePickerDialogData.copy(
                        currentDay = tempDate,
                        timeStart = getCorrectTime(time = tempTimeStart),
                        timeEnd = getCorrectTime(time = tempTimeEnd),
                    ),
                    timeStartPickerDialogData = getTimePickerDialogData(time = tempTimeStart),
                    timeEndPickerDialogData = getTimePickerDialogData(time = tempTimeEnd),
                )
            }

            is NewTaskEvent.TimeEnd -> {
                tempTimeEnd = getCorrectTime(hour = event.hour, minute = event.minute) ?: tempTimeEnd

                if (tempTimeEnd != null && tempTimeStart == null) {
                    tempTimeStart = getCorrectTime(hour = event.hour, minute = (event.minute ?: 0) + 30)
                }

                updateTime()

                state.copy(
                    dialogType = DialogType.DatePicker,
                    datePickerDialogData = state.datePickerDialogData.copy(
                        currentDay = tempDate,
                        timeStart = getCorrectTime(time = tempTimeStart),
                        timeEnd = getCorrectTime(time = tempTimeEnd),
                    ),
                    timeStartPickerDialogData = getTimePickerDialogData(time = tempTimeStart),
                    timeEndPickerDialogData = getTimePickerDialogData(time = tempTimeEnd),
                )
            }
        }
    }

    override suspend fun handleEvent(event: NewTaskEvent, state: NewTaskState): NewTaskEvent? {
        if (event is NewTaskEvent.SaveTask) {
            triggerSingleEvent(
                NewTaskSingleEvent.Save(
                    taskData = TaskData(
                        message = TaskMessage(
                            title = title,
                            description = description,
                        ),
                        date = TaskDate(
                            timeStart = timeStart ?: getCurrentTime(),
                            timeEnd = timeEnd ?: (getCurrentTime() + 100),
                            date = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        ),
                        priority = priority,
                        category = category
                    )
                )
            )

            return NewTaskEvent.OnBack
        }

        return null
    }

    private fun LocalDate.convertDate(context: Context): String {
        return when (this) {
            LocalDate.now() -> context.getString(com.res.R.string.today)
            LocalDate.now().plusDays(1) -> context.getString(com.res.R.string.tomorrow)
            LocalDate.now().plusDays(2) -> context.getString(com.res.R.string.tow_days_later)

            LocalDate.now().minusDays(1) -> context.getString(com.res.R.string.yesterday)
            LocalDate.now().minusDays(2) -> context.getString(com.res.R.string.tow_days_before)

            else -> this.format(
                DateTimeFormatter.ofPattern("MMM d")
            )
        }.replaceFirstChar { it.titlecaseChar() }
    }

    /*private fun getCorrectTimeSting(hour: Int?, minute: Int?): String {
        if (hour == null) return ""

        val correctMinute = if ("$minute".length < 2) "0${minute}" else "$minute"

        return "${hour}:$correctMinute"
    }*/

    private fun getCurrentTime(): Int {
        val format = SimpleDateFormat("HH", Locale.getDefault())
        return "${format.format(System.currentTimeMillis())}00".toInt()
    }

    private fun getCorrectTime(hour: Int?, minute: Int?): Int? {
        if (hour == null) return null
        if (hour < 0) return 0

        val correctMinute = if ("$minute".length < 2) "0${minute}" else "$minute"

        return "${hour}$correctMinute".toInt()
    }

    private fun updateTime() {
        if (tempTimeStart == null && tempTimeEnd == null) return

        val tStart = tempTimeStart ?: 0
        val tEnd = tempTimeEnd ?: 0

        when {
            tEnd < tStart || tEnd == tStart -> tempTimeEnd = (tempTimeStart ?: 0) + 30

            tEnd - tStart < 30 -> tempTimeEnd = (tempTimeEnd ?: 0) + 30 - (tEnd - tStart)
        }
    }

    private fun getCorrectTime(time: Int?): String {
        return when (time) {
            null -> ""

            0 -> "0:00"

            else -> {
                when ("$time".length) {
                    in 0..1 -> "0:0$time"
                    in 0..2 -> "0:$time"

                    in 0..3 -> {
                        val list = "$time".toList()
                        "${list[0]}:${list[1]}${list[2]}"
                    }

                    else -> {
                        val list = "$time".toList()
                        "${list[0]}${list[1]}:${list[2]}${list[3]}"
                    }
                }
            }
        }
    }

    private fun getTimePickerDialogData(time: Int?): TimePickerDialogData {
        val timeList = getCorrectTime(time = time).split(":")

        return TimePickerDialogData(
            initialHour = timeList.getOrNull(0)?.toIntOrNull() ?: 0,
            initialMinute = timeList.getOrNull(1)?.toIntOrNull() ?: 0,
        )
    }
}