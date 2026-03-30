package com.example.classschedule.data.schedule

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourseTime(schedule: Schedule)

    @Update
    suspend fun update(schedule: Schedule)

    @Update
    suspend fun updateAll(schedules: List<Schedule>)

    @Delete
    suspend fun delete(schedule: Schedule)

    @Query("SELECT * FROM schedule")
    fun getAllScheduleFlow(): Flow<List<Schedule>>

    @Query("DELETE FROM schedule WHERE  courseNumber = :courseNumber")
    suspend fun deleteByCourseNumber(courseNumber: Int)
}