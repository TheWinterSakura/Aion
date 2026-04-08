package com.example.classschedule.data.schedule

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(table: TimeTable): Long

    @Update
    suspend fun update(table: TimeTable)

    @Delete
    suspend fun delete(table: TimeTable)

    @Query("SELECT * FROM time_table ORDER BY id ASC")
    fun getAll(): Flow<List<TimeTable>>

    @Query("SELECT * FROM time_table WHERE id = :id")
    suspend fun getById(id: Int): TimeTable?
}
