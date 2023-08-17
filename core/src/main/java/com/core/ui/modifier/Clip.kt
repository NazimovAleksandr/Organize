package com.core.ui.modifier

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Stable
fun Modifier.clipCircleShape() = clip(shape = CircleShape)