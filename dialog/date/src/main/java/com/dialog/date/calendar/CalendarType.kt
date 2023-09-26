package com.dialog.date.calendar

sealed interface CalendarType {
    object Month : CalendarType
    object Week : CalendarType
}
