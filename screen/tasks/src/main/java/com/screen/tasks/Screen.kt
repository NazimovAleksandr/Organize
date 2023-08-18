package com.screen.tasks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.core.nav_graph.AppNavGraph
import com.core.ui.composable.ActionProgress
import com.core.ui.composable.Content
import com.core.ui.modifier.clickableSingle
import com.core.ui.modifier.clipCircleShape
import com.organize.entity.Habit
import com.screen.tasks.horizontal.calendar.HorizontalCalendar
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun TasksScreen(
    navigate: (AppNavGraph) -> Unit,
    viewModel: TasksViewModel = koinViewModel(),
) {
    Screen(
        state = viewModel.uiState,
        onClick = viewModel::sendEvent,
    )
}

@Composable
private fun Screen(
    state: State<TasksState>,
    onClick: (TasksEvent) -> Unit,
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

    val selectedDate = remember(key1 = state) {
        derivedStateOf {
            state.value.selectedDate
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
    ) {
        ScreenToolbar(title = title)

        Habits(habits = habits)

        HorizontalCalendar(
            selectedDate = selectedDate,
            onClickItem = { date -> onClick.invoke(TasksEvent.SelectDate(date)) }
        )

        Spacer(modifier = Modifier.height(height = 10.dp))

        Button(
            onClick = {
                onClick.invoke(TasksEvent.UpdateHabits)
            }
        ) {
            Text(
                text = "UpdateHabits",
                modifier = Modifier
            )
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
                text = titleValue,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
        },
        actions = {
            Image(
                imageVector = ImageVector.vectorResource(id = com.core.R.drawable.ic_calendar),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 1.dp)
                    .clipCircleShape()
                    .clickableSingle { } // TODO:
                    .padding(all = 6.dp)
            )

            Image(
                imageVector = ImageVector.vectorResource(id = com.core.R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 1.dp)
                    .clipCircleShape()
                    .clickableSingle { } // TODO:
                    .padding(all = 6.dp)
            )

            Image(
                imageVector = ImageVector.vectorResource(id = com.core.R.drawable.ic_menu),
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
    habits: State<List<Habit>>,
) {
    val habitsValue by remember(key1 = habits) { habits }

    Content {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            items(items = habitsValue, key = { it.id }) { habit ->
                ActionProgress(icon = habit.icon, progress = habit.progress)
            }
        }
    }
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
