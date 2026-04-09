package com.example.classschedule.data.schedule

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

interface ScheduleRepository {

    suspend fun insertCourseTime(schedule: Schedule)

    suspend fun update(schedule: Schedule)

    suspend fun delete(schedule: Schedule)

    suspend fun updateAll(scheduleList: List<Schedule>)

    fun getAllScheduleFlow(tableId: Int = 1): StateFlow<List<Schedule>>

    suspend fun deleteByCourseNumber(courseNumber: Int, tableId: Int = 1)

    suspend fun deleteAll()

    suspend fun deleteAllByTableId(tableId: Int)
}

class OfflineScheduleRepository(
    private val scheduleDao: ScheduleDao,
    private val appScope: CoroutineScope
) : ScheduleRepository {

    override suspend fun insertCourseTime(schedule: Schedule) =
        scheduleDao.insertCourseTime(schedule)

    override suspend fun update(schedule: Schedule) = scheduleDao.update(schedule)

    override suspend fun delete(schedule: Schedule) = scheduleDao.delete(schedule)

    override fun getAllScheduleFlow(tableId: Int): StateFlow<List<Schedule>> =
        scheduleDao.getAllScheduleFlow(tableId).stateIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    override suspend fun updateAll(scheduleList: List<Schedule>) = scheduleDao.updateAll(scheduleList)

    override suspend fun deleteByCourseNumber(courseNumber: Int, tableId: Int) =
        scheduleDao.deleteByCourseNumber(courseNumber, tableId)

    override suspend fun deleteAll() = scheduleDao.deleteAll()

    override suspend fun deleteAllByTableId(tableId: Int) =
        scheduleDao.deleteAllByTableId(tableId)
}