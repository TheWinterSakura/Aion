package com.example.classschedule.data.course

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

    @Query("SELECT courseName, courseTime, courseLocation, id, weekDay, color FROM course WHERE startWeekDate <= :currentWeekDate AND endWeekDate >= :currentWeekDate")
    fun getAllCourseSimple(currentWeekDate: Int): Flow<List<CourseSimple>>

    @Query("SELECT * FROM course WHERE id = :id")
    fun getCourseById(id: Int): Flow<Course?>

    @Query("DELETE FROM course")
    suspend fun deleteAll()

    @Query("SELECT courseName, courseTime, courseLocation, id, weekDay, color FROM course WHERE startWeekDate <= :currentWeekDate AND endWeekDate >= :currentWeekDate AND weekDay = :today")
    fun getTodayCourseSimple(currentWeekDate: Int, today: String): Flow<List<CourseSimple>>

    @Query("SELECT * FROM course")
    suspend fun getAllCourse(): List<Course>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourseList(courseList: List<Course>)

    @Query("UPDATE course SET color = :color WHERE id = :id")
    suspend fun updateColor(id: Int, color: String?)
}