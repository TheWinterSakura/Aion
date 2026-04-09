package com.example.classschedule.data.course

import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    fun getAllICourseSimple(currentWeekDate: Int, tableId: Int = 1): Flow<List<CourseSimple>>

    fun getCourseStream(id: Int): Flow<Course?>

    suspend fun insertCourse(item: Course)

    suspend fun deleteCourse(item: Course)

    suspend fun updateCourse(item: Course)

    suspend fun deleteAll()

    fun getTodayCourseSimple(currentWeekDate: Int, today: String, tableId: Int = 1): Flow<List<CourseSimple>>

    suspend fun getAllCourse(): List<Course>

    suspend fun insertCourseList(courseList: List<Course>)

    suspend fun updateCourseColor(id: Int, color: String?)

    fun getDistinctCourseNames(tableId: Int = 1): Flow<List<String>>

    fun getDistinctTeachers(tableId: Int = 1): Flow<List<String>>

    fun getCoursesByName(name: String, tableId: Int = 1): Flow<List<Course>>

    fun getCoursesByTeacher(teacher: String, tableId: Int = 1): Flow<List<Course>>
}

class OfflineCourseRepository(private val courseDao: CourseDao) : CourseRepository {
    override fun getAllICourseSimple(currentWeekDate: Int, tableId: Int): Flow<List<CourseSimple>> =
        courseDao.getAllCourseSimple(currentWeekDate = currentWeekDate, tableId = tableId)

    override fun getCourseStream(id: Int): Flow<Course?> = courseDao.getCourseById(id)

    override suspend fun insertCourse(item: Course) = courseDao.insertCourse(item)

    override suspend fun deleteCourse(item: Course) = courseDao.delete(item)

    override suspend fun updateCourse(item: Course) = courseDao.update(item)

    override suspend fun deleteAll() = courseDao.deleteAll()

    override fun getTodayCourseSimple(
        currentWeekDate: Int,
        today: String,
        tableId: Int,
    ): Flow<List<CourseSimple>> =
        courseDao.getTodayCourseSimple(currentWeekDate, today, tableId)

    override suspend fun getAllCourse(): List<Course> =
        courseDao.getAllCourse()

    override suspend fun insertCourseList(courseList: List<Course>) =
        courseDao.insertCourseList(courseList = courseList)

    override suspend fun updateCourseColor(id: Int, color: String?) =
        courseDao.updateColor(id, color)

    override fun getDistinctCourseNames(tableId: Int): Flow<List<String>> =
        courseDao.getDistinctCourseNames(tableId)

    override fun getDistinctTeachers(tableId: Int): Flow<List<String>> =
        courseDao.getDistinctTeachers(tableId)

    override fun getCoursesByName(name: String, tableId: Int): Flow<List<Course>> =
        courseDao.getCoursesByName(name, tableId)

    override fun getCoursesByTeacher(teacher: String, tableId: Int): Flow<List<Course>> =
        courseDao.getCoursesByTeacher(teacher, tableId)
}