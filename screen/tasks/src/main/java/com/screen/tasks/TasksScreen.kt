package com.screen.tasks

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.core.nav_graph.AppNavGraph
import com.core.ui.composable.ActionProgress
import com.core.ui.composable.Content
import com.core.ui.modifier.clickableSingle
import com.core.ui.modifier.clipCircleShape
import com.dialog.newtask.NewTaskDialog
import com.organize.entity.habit.HabitUI
import com.res.R
import com.screen.tasks.horizontal.calendar.HorizontalCalendar
import com.screen.tasks.vertical.task.list.VerticalTaskList
import com.screen.tasks.vertical.task.list.VerticalTaskListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun TasksScreen(
    navigate: (AppNavGraph) -> Unit,
    viewModel: TasksViewModel = koinViewModel(),
) {
    var newTaskDialog by remember {
        mutableStateOf(false)
    }

    Screen(
        state = viewModel.uiState,
        onClick = viewModel::sendEvent,
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.singleEvent.onEach {
            when (it) {
                TasksScreenSingleEvent.NewTask -> {
                    newTaskDialog = true
                }
            }
        }.collect()
    }

    if (newTaskDialog) {
        NewTaskDialog(
            navigate = navigate,
            dismiss = { newTaskDialog = false },
            saveTask = { viewModel.sendEvent(TasksScreenEvent.CreateTask(taskData = it)) }
        )
    }
}

@Composable
private fun Screen(
    state: State<TasksScreenState>,
    onClick: (TasksScreenEvent) -> Unit,
) {
    val title = remember(key1 = state) {
        derivedStateOf {
            state.value.date
        }
    }

    val habits = remember(key1 = state) {
        derivedStateOf {
            state.value.habits
        }
    }

    val taskListState = remember(key1 = state) {
        derivedStateOf {
            state.value.taskListState
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.scrim,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(pressedElevation = 3.dp),
                onClick = {
                    onClick.invoke(TasksScreenEvent.AddTask)
                },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        shape = FloatingActionButtonDefaults.shape,
                        color = MaterialTheme.colorScheme.surface
                    )
            ) {
                val color = MaterialTheme.colorScheme.surface

                Canvas(
                    modifier = Modifier
                        .size(size = 20.dp)
                ) {
                    drawLine(
                        color = color,
                        start = Offset(x = size.width / 2f, y = 0f),
                        end = Offset(x = size.width / 2f, y = size.height),
                        strokeWidth = 10f,
                        cap = StrokeCap.Round,
                        alpha = 1.0f,
                        pathEffect = null,
                        colorFilter = null,
                        blendMode = DefaultBlendMode
                    )

                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = size.height / 2f),
                        end = Offset(x = size.width, y = size.height / 2f),
                        strokeWidth = 10f,
                        cap = StrokeCap.Round,
                        alpha = 1.0f,
                        pathEffect = null,
                        colorFilter = null,
                        blendMode = DefaultBlendMode
                    )
                }
            }
        }
    ) {
        it.let {}

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
        ) {
            ScreenToolbar(title = title)

            Habits(habits = habits)

            Content {
                HorizontalCalendar(
                    onClickItem = { date -> onClick.invoke(TasksScreenEvent.SelectDate(date)) }
                )
            }

            TaskList(taskListState = taskListState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenToolbar(title: State<String>) {
    val titleValue by remember(key1 = title) { title }

    TopAppBar(
        title = {
            Text(
                text = "$titleValue, ${stringResource(id = R.string.today)}",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
        },
        actions = {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_calendar),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 1.dp)
                    .clipCircleShape()
                    .clickableSingle { } // TODO:
                    .padding(all = 6.dp)
            )

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 1.dp)
                    .clipCircleShape()
                    .clickableSingle { } // TODO:
                    .padding(all = 6.dp)
            )

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_menu),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clipCircleShape()
                    .clickableSingle { } // TODO:
                    .padding(all = 6.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        // scrollBehavior = null // TODO:
    )
}

@Composable
private fun Habits(
    habits: State<Flow<List<HabitUI>>>,
) {
    val habitsValue by habits.value.collectAsStateWithLifecycle(initialValue = emptyList())

    Content {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            items(items = habitsValue, key = { it.id }) { habit ->
                ActionProgress(icon = habit.icon.value, progress = habit.progress)
            }
        }
    }
}

@Composable
private fun TaskList(
    taskListState: State<VerticalTaskListState>,
) {
    val taskList by remember(key1 = taskListState) { taskListState }

    VerticalTaskList(state = taskList)
}
/*

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    OrganizeTheme {
        Screen(
            state = TasksState(
                date = "1 июня, сег.",
                habits = listOf(
                    Habit(
                        id = 1,
                        icon = com.core.R.drawable.ic_water,
                        progress = 0.3f
                    ),
                    Habit(
                        id = 2,
                        icon = com.core.R.drawable.ic_education,
                        progress = 0.6f
                    ),
                    Habit(
                        id = 3,
                        icon = com.core.R.drawable.ic_sport,
                        progress = 0.8f
                    ),
                ),
                selectedDate = 0L
            ),
            onClick = {}
        )
    }
}*/
