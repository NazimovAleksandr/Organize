package com.screen.tasks.vertical.task.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.core.ui.theme.PrimaryText
import com.core.ui.theme.PriorityCardHigh
import com.core.ui.theme.PriorityCardLow
import com.core.ui.theme.PriorityCardMedium
import com.core.ui.theme.PriorityCardNotAssigned
import com.organize.entity.task.TaskUI
import com.organize.entity.task_priority.TaskPriority

@Composable
fun TaskCard(
    task: TaskUI,
    modifier: Modifier = Modifier,
) {
    val color = when (task.priority) {
        TaskPriority.High -> PriorityCardHigh
        TaskPriority.Medium -> PriorityCardMedium
        TaskPriority.Low -> PriorityCardLow
        TaskPriority.No -> PriorityCardNotAssigned
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .clip(shape = RoundedCornerShape(size = 8.dp))
            .background(color = color)
            .padding(horizontal = 12.dp)
    ) {
        when (val icon = task.category?.icon?.value) {
            null -> {
                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                            letterSpacing = MaterialTheme.typography.bodyMedium.letterSpacing,
                            textDecoration = if (task.done) TextDecoration.LineThrough else null,
                        )
                    ) {
                        append(task.message.title)
                    }
                }

                Text(
                    text = annotatedString,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                    color = if (task.done) PriorityCardNotAssigned else PrimaryText,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }

            else -> {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val annotatedString = buildAnnotatedString {
                        appendInlineContent(id = "imageId")

                        append("  ")

                        withStyle(
                            style = SpanStyle(
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                letterSpacing = MaterialTheme.typography.bodyMedium.letterSpacing,
                                textDecoration = if (task.done) TextDecoration.LineThrough else null,
                            )
                        ) {
                            append(task.message.title)
                        }
                    }

                    val inlineContentMap = mapOf(
                        "imageId" to InlineTextContent(
                            placeholder = Placeholder(14.sp, 14.sp, PlaceholderVerticalAlign.TextCenter)
                        ) {
                            Image(
                                painter = painterResource(id = icon),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = if (task.done) PriorityCardNotAssigned else PrimaryText),
                                modifier = Modifier
                                    .height(height = 16.dp)
                                    .width(width = 16.dp)
                            )
                        }
                    )

                    Text(
                        text = annotatedString,
                        inlineContent = inlineContentMap,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight,
                        color = if (task.done) PriorityCardNotAssigned else PrimaryText,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                }
            }
        }
    }
}