package com.screen.tasks.vertical.task.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.ui.composable.Content
import com.core.ui.theme.OrganizeTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VerticalTaskList(
    state: VerticalTaskListState,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Content {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(state = scrollState)
                .padding(bottom = 8.dp)
        ) {
            HourCount()

            var maxHeight by remember {
                mutableStateOf(0.dp)
            }

            val density = LocalDensity.current

            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .weight(weight = 1f)
                    .clip(shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .onGloballyPositioned {
                        with(density) {
                            maxHeight = it.size.height.toDp()
                        }
                    }
            ) {
                TaskBG()

                if (maxHeight != 0.dp) {
                    Tasks(
                        tasksFlow = state.tasks,
                        tasksType = state.tasksType,
                        selectedDate = state.selectedDate,
                        height = maxHeight
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        val currentHour = SimpleDateFormat("HH", Locale.getDefault())
            .format(Date())
            .toInt()

        val currentMinutes = SimpleDateFormat("mm", Locale.getDefault())
            .format(Date())
            .toInt()
            .let { 100f / 60f * it }.toInt()
            .let { if ("$it".length == 1) "0$it" else "$it" }

        val currentTime = "$currentHour$currentMinutes".toFloat()

        var topWeight = currentTime - 10f
        if (topWeight < 0f) topWeight = 0f

        scrollState.scrollTo(topWeight.toInt())
    }
}

@Composable
private fun HourCount() {
    Content {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(horizontal = 5.dp)
        ) {
            var hourTextHeight by remember {
                mutableIntStateOf(0)
            }

            val density = LocalDensity.current

            repeat(25) { hourCount ->
                if (hourCount == 0) return@repeat

                val hour =
                    if ("$hourCount".length == 1) "0$hourCount"
                    else "$hourCount"

                val height =
                    if (hourCount == 1) 50.dp + with(density) { hourTextHeight.toDp() } - 1.dp
                    else 50.dp

                Box(
                    contentAlignment = Alignment.BottomStart,
                    modifier = Modifier
                        .height(height = height)
                ) {
                    Text(
                        text = hour,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .let { modifier ->
                                if (hourCount == 1) modifier.onGloballyPositioned { coordinates ->
                                    hourTextHeight = coordinates.size.height / 2
                                }
                                else modifier
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskBG() {
    Content {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 2.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
        ) {
            repeat(24) {
                Hour(it)
            }
        }
    }
}

@Composable
private fun Hour(number: Int) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = if (number == 23) 50.dp else 48.dp)
            .background(color = MaterialTheme.colorScheme.scrim)
    )
}

@Preview
@Composable
private fun Preview() {
    OrganizeTheme {
//        VerticalTaskList()
    }
}