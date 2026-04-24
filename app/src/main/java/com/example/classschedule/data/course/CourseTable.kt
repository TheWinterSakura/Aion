package com.example.classschedule.data.course

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_table")
data class CourseTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
