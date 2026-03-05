package com.example.classschedule.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun update(course: Course)

    @Delete
    suspend fun delete(course: Course)

    @Query("SELECT courseName, courseTime, courseLocation, id, weekDay FROM course WHERE startWeekDate <= :currentWeekDate AND endWeekDate >= :currentWeekDate")
    fun getAllCourseSimple(currentWeekDate: Int): Flow<List<CourseSimple>>

    @Query("SELECT * FROM course WHERE id = :id")
    fun getCourseById(id: Int): Flow<Course?>

    @Query("DELETE FROM course")
    suspend fun deleteAll()
}