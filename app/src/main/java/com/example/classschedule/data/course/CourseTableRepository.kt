package com.example.classschedule.data.course

import kotlinx.coroutines.flow.Flow

interface CourseTableRepository {
    fun getAll(): Flow<List<CourseTable>>
    suspend fun getById(id: Int): CourseTable?
    suspend fun insert(table: CourseTable): Long
    suspend fun update(table: CourseTable)
    suspend fun delete(table: CourseTable)
}

class OfflineCourseTableRepository(
    private val dao: CourseTableDao
) : CourseTableRepository {
    override fun getAll(): Flow<List<CourseTable>> = dao.getAll()
    override suspend fun getById(id: Int): CourseTable? = dao.getById(id)
    override suspend fun insert(table: CourseTable): Long = dao.insert(table)
    override suspend fun update(table: CourseTable) = dao.update(table)
    override suspend fun delete(table: CourseTable) = dao.delete(table)
}
