package com.dialog.newtask

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.core.nav_graph.AppNavGraph
import com.core.ui.composable.BasicTextFieldColors
import com.core.ui.composable.BasicTextFieldWithPlaceholder
import com.core.ui.composable.Content
import com.core.ui.modifier.clickableSingle
import com.core.ui.theme.PriorityHigh
import com.core.ui.theme.PriorityLow
import com.core.ui.theme.PriorityMedium
import com.core.ui.utils.keyboardHeightWithoutNavBar
import com.dialog.category.CategoryPickerDialog
import com.dialog.date.DatePickerDialog
import com.dialog.date.DatePickerDialogCommand
import com.dialog.priority.PriorityPickerDialog
import com.dialog.time.TimePickerDialog
import com.organize.entity.task_priority.TaskPriority
import com.res.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.compose.koinInject

sealed interface NewTaskDialogCommand {
    object FullSize : NewTaskDialogCommand
    object Date : NewTaskDialogCommand
    object Priority : NewTaskDialogCommand
    object Category : NewTaskDialogCommand
    object SaveTask : NewTaskDialogCommand
    class Title(val data: String) : NewTaskDialogCommand
    class Description(val data: String) : NewTaskDialogCommand
}

@Composable
fun NewTaskDialog(
    navigate: (AppNavGraph) -> Unit,
    dismiss: () -> Unit,
    saveTask: (TaskData) -> Unit,
    viewModel: NewTaskViewModel = koinInject(),
) {
    val context = LocalContext.current

    val state by remember { viewModel.uiState }

    val paddingBottom = keyboardHeightWithoutNavBar()

    Content {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.4f))
                .padding(bottom = paddingBottom)
        ) {
            Dialog(
                state = state,
                viewModel = viewModel,
                dismiss = dismiss,
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.singleEvent.onEach {
            when (it) {
                is NewTaskSingleEvent.Save -> saveTask.invoke(it.taskData)
            }
        }.collect()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.sendEvent(event = NewTaskEvent.LoadDate(context = context))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ColumnScope.Dialog(
    state: NewTaskState,
    viewModel: NewTaskViewModel,
    dismiss: () -> Unit,
) {
    BackHandler {
        viewModel.sendEvent(NewTaskEvent.OnBack)
    }

    when (state.dialogType) {
        DialogType.Back -> LaunchedEffect(key1 = Unit, block = { dismiss.invoke() })

        DialogType.FullSize -> {} // todo

        DialogType.DatePicker -> {
            LocalSoftwareKeyboardController.current?.hide()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clickableSingle(ripple = false) { viewModel.sendEvent(NewTaskEvent.OnBack) }
            ) {
                val context = LocalContext.current

                DatePickerDialog(
                    data = state.datePickerDialogData
                ) {
                    when (it) {
                        is DatePickerDialogCommand.TimeStart -> viewModel.sendEvent(NewTaskEvent.OpenTimeStartPicker)
                        is DatePickerDialogCommand.TimeEnd -> viewModel.sendEvent(NewTaskEvent.OpenTimeEndPicker)

                        is DatePickerDialogCommand.Recall -> {} // todo
                        is DatePickerDialogCommand.Repeat -> {} // todo

                        is DatePickerDialogCommand.Clear -> viewModel.sendEvent(NewTaskEvent.ClearDate(context))
                        is DatePickerDialogCommand.Cancel -> viewModel.sendEvent(NewTaskEvent.CancelDate)

                        is DatePickerDialogCommand.Ok -> viewModel.sendEvent(NewTaskEvent.SelectDate(context, it.date))
                        is DatePickerDialogCommand.Select -> viewModel.sendEvent(NewTaskEvent.TempDate(it.date))
                    }
                }
            }
        }

        DialogType.TimePickerStart -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clickableSingle(ripple = false) { viewModel.sendEvent(NewTaskEvent.OnBack) }
            ) {
                TimePickerDialog(
                    data = state.timeStartPickerDialogData,
                    onDismiss = { hour, minute ->
                        viewModel.sendEvent(NewTaskEvent.TimeStart(hour, minute))
                    }
                )
            }
        }

        DialogType.TimePickerEnd -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clickableSingle(ripple = false) { viewModel.sendEvent(NewTaskEvent.OnBack) }
            ) {
                TimePickerDialog(
                    data = state.timeEndPickerDialogData,
                    onDismiss = { hour, minute ->
                        viewModel.sendEvent(NewTaskEvent.TimeEnd(hour, minute))
                    }
                )
            }
        }

        else -> {
            val focusRequester = remember { FocusRequester() }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { viewModel.sendEvent(NewTaskEvent.OnBack) }
                    )
            )

            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
            ) {
                BottomDialog(
                    data = state.newTaskDialogData,
                    focusRequester = focusRequester,
                ) {
                    when (it) {
                        is NewTaskDialogCommand.FullSize -> viewModel.sendEvent(NewTaskEvent.OpenFullSize)
                        is NewTaskDialogCommand.Date -> viewModel.sendEvent(NewTaskEvent.OpenDatePicker)
                        is NewTaskDialogCommand.Priority -> viewModel.sendEvent(NewTaskEvent.OpenPriority)
                        is NewTaskDialogCommand.Category -> viewModel.sendEvent(NewTaskEvent.OpenCategory)
                        is NewTaskDialogCommand.Title -> viewModel.sendEvent(NewTaskEvent.Title(it.data))
                        is NewTaskDialogCommand.Description -> viewModel.sendEvent(NewTaskEvent.Description(it.data))
                        is NewTaskDialogCommand.SaveTask -> viewModel.sendEvent(NewTaskEvent.SaveTask)
                    }
                }

                when (state.dialogType) {
                    DialogType.Priority -> {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .fillMaxHeight()
                                .clickableSingle(ripple = false) {
                                    viewModel.sendEvent(NewTaskEvent.Priority(priority = null))
                                }
                        ) {
                            Spacer(modifier = Modifier.weight(weight = 1f))

                            PriorityPickerDialog(
                                select = {
                                    viewModel.sendEvent(NewTaskEvent.Priority(priority = it))
                                }
                            )

                            Spacer(modifier = Modifier.weight(weight = 2f))
                        }
                    }

                    DialogType.Category -> {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 16.dp)
                                .clickableSingle(ripple = false) {
                                    viewModel.sendEvent(NewTaskEvent.Category(category = null))
                                }
                        ) {
                            Spacer(modifier = Modifier.weight(weight = 2f))

                            CategoryPickerDialog(
                                list = state.categoryList,
                                add = {}, // todo
                                select = {
                                    viewModel.sendEvent(NewTaskEvent.Category(category = it))
                                }
                            )

                            Spacer(modifier = Modifier.weight(weight = 3f))
                        }
                    }

                    else -> {}
                }
            }

            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}

@Composable
private fun BottomDialog(
    data: NewTaskDialogData,
    focusRequester: FocusRequester,
    onClick: (NewTaskDialogCommand) -> Unit,
) {
    var taskTitle by remember {
        mutableStateOf(data.title)
    }

    var taskDescription by remember {
        mutableStateOf(data.description)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(color = MaterialTheme.colorScheme.background)
            .pointerInput(key1 = Unit) {}
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(height = 36.dp)
                .width(width = 32.dp)
                .padding(vertical = 16.dp)
                .clip(shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.outline)
                .align(alignment = Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
        ) {
            BasicTextFieldWithPlaceholder(
                value = taskTitle,
                onValueChange = {
                    taskTitle = it
                    onClick.invoke(NewTaskDialogCommand.Title(data = taskTitle))
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.write_a_task),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                    )
                },
                textStyle = MaterialTheme.typography.titleMedium,
                colors = BasicTextFieldColors(
                    focusedTextColor = MaterialTheme.colorScheme.outline,
                    unfocusedTextColor = MaterialTheme.colorScheme.outline,
                ),
                modifier = Modifier
                    .weight(weight = 1f)
                    .focusRequester(focusRequester)
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_increase),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clip(shape = RoundedCornerShape(size = 6.dp))
                    .clickableSingle(onClick = { onClick(NewTaskDialogCommand.FullSize) })
            )
        }

        Spacer(modifier = Modifier.height(height = 4.dp))

        BasicTextFieldWithPlaceholder(
            value = taskDescription,
            onValueChange = {
                taskDescription = it
                onClick.invoke(NewTaskDialogCommand.Description(data = taskDescription))
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = BasicTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.outline,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .alpha(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(height = 16.dp))

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 6.dp))
                    .clickableSingle(onClick = { onClick(NewTaskDialogCommand.Date) })
            ) {
                val color = when (data.negativeDate) {
                    true -> PriorityHigh
                    false -> MaterialTheme.colorScheme.outline
                }

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_calendar),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.width(width = 4.dp))

                Text(
                    text = data.descriptionDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = color,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.width(width = 16.dp))

            val tintPriority = when (data.priority) {
                TaskPriority.High -> PriorityHigh
                TaskPriority.Medium -> PriorityMedium
                TaskPriority.Low -> PriorityLow
                else -> MaterialTheme.colorScheme.outline
            }

            val iconPriority = when (data.priority) {
                null -> R.drawable.ic_priority_task
                else -> R.drawable.ic_priority_task_full
            }

            Icon(
                imageVector = ImageVector.vectorResource(id = iconPriority),
                contentDescription = null,
                tint = tintPriority,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 6.dp))
                    .clickableSingle(onClick = { onClick(NewTaskDialogCommand.Priority) })
            )

            Spacer(modifier = Modifier.width(width = 16.dp))

            val iconCategory = when (data.category) {
                null -> R.drawable.ic_group_task
                else -> data.category.icon.value
            }

            Icon(
                imageVector = ImageVector.vectorResource(id = iconCategory),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 6.dp))
                    .clickableSingle(onClick = { onClick(NewTaskDialogCommand.Category) })
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            when {
                taskTitle.isEmpty() -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_save_task),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                    )
                }

                else -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_save_task),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(size = 6.dp))
                            .clickableSingle(onClick = { onClick(NewTaskDialogCommand.SaveTask) })
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(height = 12.dp))
    }
}