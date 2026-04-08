package com.example.classschedule.data.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_table")
data class TimeTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)
