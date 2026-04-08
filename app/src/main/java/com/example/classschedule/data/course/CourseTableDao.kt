package com.example.classschedule.data.course

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(table: CourseTable): Long

    @Update
    suspend fun update(table: CourseTable)

    @Delete
    suspend fun delete(table: CourseTable)

    @Query("SELECT * FROM course_table ORDER BY id ASC")
    fun getAll(): Flow<List<CourseTable>>

    @Query("SELECT * FROM course_table WHERE id = :id")
    suspend fun getById(id: Int): CourseTable?
}
