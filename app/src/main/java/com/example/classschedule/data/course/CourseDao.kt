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

    @Query("SELECT courseName, courseTime, courseLocation, id, weekDay, color, tableId FROM course WHERE startWeekDate <= :currentWeekDate AND endWeekDate >= :currentWeekDate AND tableId = :tableId")
    fun getAllCourseSimple(currentWeekDate: Int, tableId: Int = 1): Flow<List<CourseSimple>>

    @Query("SELECT * FROM course WHERE id = :id")
    fun getCourseById(id: Int): Flow<Course?>

    @Query("DELETE FROM course")
    suspend fun deleteAll()

    @Query("SELECT courseName, courseTime, courseLocation, id, weekDay, color, tableId FROM course WHERE startWeekDate <= :currentWeekDate AND endWeekDate >= :currentWeekDate AND weekDay = :today AND tableId = :tableId")
    fun getTodayCourseSimple(currentWeekDate: Int, today: String, tableId: Int = 1): Flow<List<CourseSimple>>

    @Query("SELECT * FROM course")
    suspend fun getAllCourse(): List<Course>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourseList(courseList: List<Course>)

    @Query("UPDATE course SET color = :color WHERE id = :id")
    suspend fun updateColor(id: Int, color: String?)

    @Query("SELECT DISTINCT courseName FROM course WHERE tableId = :tableId ORDER BY courseName ASC")
    fun getDistinctCourseNames(tableId: Int = 1): Flow<List<String>>

    @Query("SELECT DISTINCT courseTeacher FROM course WHERE courseTeacher != '' AND tableId = :tableId ORDER BY courseTeacher ASC")
    fun getDistinctTeachers(tableId: Int = 1): Flow<List<String>>

    @Query("SELECT * FROM course WHERE courseName = :name AND tableId = :tableId ORDER BY weekDay ASC")
    fun getCoursesByName(name: String, tableId: Int = 1): Flow<List<Course>>

    @Query("SELECT * FROM course WHERE courseTeacher = :teacher AND tableId = :tableId ORDER BY weekDay ASC")
    fun getCoursesByTeacher(teacher: String, tableId: Int = 1): Flow<List<Course>>
}