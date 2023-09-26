package com.screen.tasks.vertical.task.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.core.ui.theme.OrganizeTheme
import com.organize.entity.icons.Icons
import com.organize.entity.task.TaskUI
import com.organize.entity.task_category.CategoryUI
import com.organize.entity.task_date.TaskDate
import com.organize.entity.task_message.TaskMessage
import com.organize.entity.task_priority.TaskPriority

@Composable
internal fun TaskLayout(
    list: List<TaskUI>,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    val sortedList = list.sortedBy { it.date.timeStart }

    val taskMap = mutableMapOf<Int, MutableList<TaskUI>>()
    val mutableList = mutableListOf<TaskUI>()
    var count = 0

    for (i in sortedList) {
        if (!mutableList.contains(i)) {
            mutableList.clear()
            mutableList.add(i)

            for (k in sortedList) {
                if (i != k) {
                    when {
                        k.date.timeStart in i.date.timeStart until i.date.timeEnd ->
                            mutableList.add(k)

                        k.date.timeStart <= i.date.timeStart && k.date.timeEnd > i.date.timeStart ->
                            mutableList.add(k)
                    }
                }
            }

            taskMap[count] = mutableList.toMutableList()
            count++
        }
    }

    val taskWidthMap = mutableMapOf<TaskUI?, Int>()

    Layout(
        modifier = modifier,
        content = {
            taskMap.values.sortedByDescending { it.size }.flatten().toSet().forEach {
                TaskCard(
                    task = it,
                    modifier = Modifier
                        .task(task = it)
                )
            }
        },
    ) { measurables, constraints ->
        val oneHour = constraints.maxHeight / 24

        val placeables = measurables.map { measurable ->
            val task = (measurable.parentData as? TaskParentData)?.task
            val timeStart = task?.date?.timeStart ?: 0
            val timeEnd = task?.date?.timeEnd ?: 0

            val itemHeight = getTaskHeight(oneHour, timeStart, timeEnd, density)

            val itemLineList = taskMap.values.sortedByDescending { it.size }.filter { it.contains(task) }.toList()[0]

            val itemLineCount = itemLineList.size
            val itemHorizontalPadding = when (itemLineCount) {
                1 -> 0f
                else -> with(density) { 2.dp.toPx() }
            }

            var itemIndex = itemLineList.indexOf(task)
            var itemWidth = 0f

            while (itemIndex < itemLineList.size) {
                taskWidthMap[itemLineList[itemIndex]]?.let { width ->
                    val itemCount = constraints.maxWidth / width

                    itemWidth = constraints.maxWidth / itemCount - itemHorizontalPadding
                }

                itemIndex++
            }

            if (itemWidth == 0f) {
                itemWidth = constraints.maxWidth / itemLineCount - itemHorizontalPadding
            }

            taskWidthMap[task] = itemWidth.toInt()

            val childConstraints = constraints.copy(
                minWidth = itemWidth.toInt(),
                maxWidth = itemWidth.toInt(),
                minHeight = itemHeight,
                maxHeight = itemHeight,
            )

            measurable.measure(constraints = childConstraints)
        }

        val layoutHeight = constraints.maxHeight

        layout(
            width = constraints.maxWidth,
            height = layoutHeight,
        ) {
            var x: Int
            var y: Int

            placeables.forEach { placeable ->
                val task = ((placeable as? Measurable)?.parentData as? TaskParentData)?.task

                val timeStart = task?.date?.timeStart ?: 0

                val hour = timeStart / 100
                val minutes = (timeStart - (hour * 100))
                    .let { 100f / 60f * it }.toInt()
                    .let { if ("$it".length == 1) "0$it" else "$it" }

                val time = "$hour$minutes".toFloat() / 100

                val topPadding = with(density) {
                    when (minutes) {
                        "50" -> 0.dp.toPx().toInt()
                        else -> 2.dp.toPx().toInt()
                    }
                }

                y = (oneHour * time + topPadding).toInt()

                val listLine = taskMap.filter { it.value.contains(task) }.values.toList()[0]
                val taskPosition = listLine.indexOf(task)

                val offset = when (val itemCount = constraints.maxWidth / placeable.width) {
                    1 -> 0
                    else -> (constraints.maxWidth - placeable.width * itemCount) / (itemCount - 1) * taskPosition
                }

                x = placeable.width * taskPosition + offset

                placeable.placeRelative(x = x, y = y)
            }
        }
    }
}

private fun getTaskHeight(
    oneHour: Int,
    timeStart: Int,
    timeEnd: Int,
    density: Density,
): Int {
//    if (timeStart == 0 && timeEnd == 0) return 0

    val timeS = timeStart.let {
        val hour = timeStart / 100

        (timeStart - (hour * 100))
            .let { 100f / 60f * it }.toInt()
            .let { if ("$it".length == 1) "${hour}0$it" else "$hour$it" }
            .toInt()
    }

    val timeE = timeEnd.let {
        val hour = timeEnd / 100

        (timeEnd - (hour * 100))
            .let { 100f / 60f * it }.toInt()
            .let { if ("$it".length == 1) "${hour}0$it" else "$hour$it" }
            .toInt()
    }

    val coefficient = (timeE - timeS) / 100f

    val timeStartMinutes = timeStart.getMinutes()
    val timeEndMinutes = timeEnd.getMinutes()

    val padding = with(density) {
        when {
            timeStartMinutes == 0 && timeEndMinutes == 0 -> 6.dp.toPx().toInt()
            timeStartMinutes == 0 && timeEndMinutes == 30 -> 4.dp.toPx().toInt()
            timeStartMinutes == 30 && timeEndMinutes == 0 -> 4.dp.toPx().toInt()
            timeStartMinutes == 30 && timeEndMinutes == 30 -> 2.5.dp.toPx().toInt()

            else -> 0.dp.toPx().toInt()
        }
    }

    return (oneHour * coefficient).toInt().let {
        if (it > padding) it - padding
        else it
    }
}

private fun Int.getMinutes(): Int {
    return "$this".toList()
        .reversed()
        .filterIndexed { index, _ -> index < 2 }
        .reversed()
        .joinToString(separator = "")
        .toInt()
}

private fun Modifier.task(task: TaskUI) =
    this.then(TaskParentData(task))

private class TaskParentData(
    val task: TaskUI,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this@TaskParentData
}

@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
        TaskLayout(
            list = listOf(
                element = TaskUI(
                    id = 0,
                    message = TaskMessage(
                        title = "Title",
                        description = "Description",
                    ),
                    date = TaskDate(
                        timeStart = 0,
                        timeEnd = 100,
                        date = System.currentTimeMillis(),
                    ),
                    priority = TaskPriority.High,
                    category = CategoryUI(
                        id = 1, icon = Icons.Water, name = "a"
                    ),
                    done = false,
                ),
            ),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}