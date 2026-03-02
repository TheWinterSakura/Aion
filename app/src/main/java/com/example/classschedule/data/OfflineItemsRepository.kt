package com.example.classschedule.data

import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    fun getAllICourseSimple(weekData: String, weekDay: String): Flow<List<CourseSimple>>


    fun getCourseStream(id: Int): Flow<Course?>


    suspend fun insertCourse(item: Course)


    suspend fun deleteCourse(item: Course)


    suspend fun updateCourse(item: Course)
}

class OfflineItemsRepository(private val courseDao: CourseDao) : CourseRepository {
    override fun getAllICourseSimple(weekData: String, weekDay: String): Flow<List<CourseSimple>> =
        courseDao.getAllCourseSimple(weekData = weekData, weekDay = weekDay)

    override fun getCourseStream(id: Int): Flow<Course?> = courseDao.getCourseById(id)

    override suspend fun insertCourse(item: Course) = courseDao.insertCourse(item)

    override suspend fun deleteCourse(item: Course) = courseDao.delete(item)

    override suspend fun updateCourse(item: Course) = courseDao.update(item)
}