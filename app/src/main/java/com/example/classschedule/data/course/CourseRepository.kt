package com.example.classschedule.data.course

import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    fun getAllICourseSimple(currentWeekDate: Int): Flow<List<CourseSimple>>


    fun getCourseStream(id: Int): Flow<Course?>


    suspend fun insertCourse(item: Course)


    suspend fun deleteCourse(item: Course)


    suspend fun updateCourse(item: Course)

    suspend fun deleteAll()
}

class OfflineCourseRepository(private val courseDao: CourseDao) : CourseRepository {
    override fun getAllICourseSimple(currentWeekDate:Int): Flow<List<CourseSimple>> =
        courseDao.getAllCourseSimple(currentWeekDate = currentWeekDate)

    override fun getCourseStream(id: Int): Flow<Course?> = courseDao.getCourseById(id)

    override suspend fun insertCourse(item: Course) = courseDao.insertCourse(item)

    override suspend fun deleteCourse(item: Course) = courseDao.delete(item)

    override suspend fun updateCourse(item: Course) = courseDao.update(item)

    override suspend fun deleteAll() = courseDao.deleteAll()
}