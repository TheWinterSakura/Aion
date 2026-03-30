package com.example.classschedule.data.schedule

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

interface ScheduleRepository {

    suspend fun insertCourseTime(schedule: Schedule)

    suspend fun update(schedule: Schedule)

    suspend fun delete(schedule: Schedule)

    fun getAllScheduleFlow(): StateFlow<List<Schedule>>
}

class OfflineScheduleRepository(
    private val scheduleDao: ScheduleDao,
    private val appScope: CoroutineScope
) : ScheduleRepository {

    override suspend fun insertCourseTime(schedule: Schedule) =
        scheduleDao.insertCourseTime(schedule)

    override suspend fun update(schedule: Schedule) = scheduleDao.update(schedule)

    override suspend fun delete(schedule: Schedule) = scheduleDao.delete(schedule)

    override fun getAllScheduleFlow(): StateFlow<List<Schedule>> =
        scheduleDao.getAllScheduleFlow().stateIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
}