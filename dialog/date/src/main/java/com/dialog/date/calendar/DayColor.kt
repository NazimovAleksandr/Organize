package com.dialog.date.calendar

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

fun Modifier.dayBackgroundColor(
    selected: Boolean,
    color: Color,
): Modifier {
    val backgroundColor = when {
        selected -> color
        else -> Color.Transparent
    }

    return this.then(
        background(backgroundColor)
    )
}
