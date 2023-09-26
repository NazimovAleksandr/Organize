package com.organize.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.organize.entity.task_date.TaskDate

class TaskDateConverter {
    @TypeConverter
    fun fromTaskDate(taskDate: TaskDate): String {
        val gson = Gson()
        val type = object : TypeToken<TaskDate>() {}.type

        return gson.toJson(taskDate, type)
    }

    @TypeConverter
    fun toTaskDate(chatBotList: String): TaskDate {
        val gson = Gson()
        val type = object : TypeToken<TaskDate>() {}.type

        return gson.fromJson(chatBotList, type)
    }
}