package com.organize.entity.task_category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.organize.entity.icons.Icons

@Entity(tableName = "category")
class CategoryDB(
    @PrimaryKey
    val id: Int,
    val icon: Icons,
    val name: String,
)
