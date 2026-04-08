package com.example.classschedule.data.schedule

import kotlinx.coroutines.flow.Flow

interface TimeTableRepository {
    fun getAll(): Flow<List<TimeTable>>
    suspend fun getById(id: Int): TimeTable?
    suspend fun insert(table: TimeTable): Long
    suspend fun update(table: TimeTable)
    suspend fun delete(table: TimeTable)
}

class OfflineTimeTableRepository(
    private val dao: TimeTableDao
) : TimeTableRepository {
    override fun getAll(): Flow<List<TimeTable>> = dao.getAll()
    override suspend fun getById(id: Int): TimeTable? = dao.getById(id)
    override suspend fun insert(table: TimeTable): Long = dao.insert(table)
    override suspend fun update(table: TimeTable) = dao.update(table)
    override suspend fun delete(table: TimeTable) = dao.delete(table)
}
