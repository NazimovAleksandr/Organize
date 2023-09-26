package com.organize.entity.habit

import androidx.annotation.FloatRange
import com.organize.entity.icons.Icons

class HabitUI(
    val id: Int,
    val icon: Icons,
    @FloatRange(from = 0.0, to = 1.0)
    val progress: Float,
)