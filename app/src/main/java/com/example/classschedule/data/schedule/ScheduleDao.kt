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

    @Query("SELECT * FROM schedule WHERE tableId = :tableId")
    fun getAllScheduleFlow(tableId: Int = 1): Flow<List<Schedule>>

    @Query("DELETE FROM schedule WHERE courseNumber = :courseNumber AND tableId = :tableId")
    suspend fun deleteByCourseNumber(courseNumber: Int, tableId: Int = 1)

    @Query("DELETE FROM schedule WHERE tableId = :tableId")
    suspend fun deleteAllByTableId(tableId: Int)

    @Query("DELETE FROM schedule")
    suspend fun deleteAll()
}