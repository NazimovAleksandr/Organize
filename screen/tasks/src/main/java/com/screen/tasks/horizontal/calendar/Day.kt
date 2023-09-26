package com.screen.tasks.horizontal.calendar

data class Day(
    val dayOfWeek: String,
    val number: String,
    val dateTime: Long,
    val isSelected: Boolean,
    val isToday: Boolean,
    val isAfterToday: Boolean,
)