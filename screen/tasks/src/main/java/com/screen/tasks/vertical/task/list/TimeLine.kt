package com.screen.tasks.vertical.task.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.ui.composable.Content
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun TimeLine(height: Dp) {
    val color = MaterialTheme.colorScheme.surface

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

    var bottomWeight = 2400f - currentTime - 10f
    if (bottomWeight < 0f) bottomWeight = 0f

    Content {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .height(height = height)
                .padding(end = 5.dp)
        ) {
            Spacer(modifier = Modifier.weight(weight = topWeight))

            Box(
                contentAlignment = Alignment.CenterStart,
            ) {
                Canvas(
                    modifier = Modifier
                        .size(size = 10.dp)
                ) {
                    val path = Path().apply {
                        lineTo(x = size.width, y = size.height / 2f)
                        lineTo(x = 0f, y = size.height)
                        close()
                    }

                    drawPath(
                        path = path,
                        color = color,
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(height = 2.dp)
                        .fillMaxWidth()
                        .background(color = color)
                )
            }

            Spacer(modifier = Modifier.weight(weight = bottomWeight))
        }
    }
}