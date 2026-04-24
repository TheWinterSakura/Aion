package com.example.classschedule.data.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val courseNumber: Int,
    val startTime: String,
    val endTime: String,
    val tableId: Int = 1,
)