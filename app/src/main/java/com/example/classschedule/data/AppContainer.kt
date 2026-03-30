package com.example.classschedule.data

import android.content.Context
import com.example.classschedule.data.schedule.OfflineScheduleRepository
import com.example.classschedule.data.schedule.ScheduleDatabase
import com.example.classschedule.data.schedule.ScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


interface AppContainer {
    val courseRepository: CourseRepository

    val scheduleRepository: ScheduleRepository

    val applicationScope: CoroutineScope
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val applicationScope: CoroutineScope = CoroutineScope(SupervisorJob())

    override val courseRepository: CourseRepository by lazy {
        OfflineCourseRepository(CourseDatabase.getDatabase(context).courseDao())
    }

    override val scheduleRepository: ScheduleRepository =
        OfflineScheduleRepository(
            ScheduleDatabase.getDatabase(context).ScheduleDao(),
            appScope = applicationScope
        )
}