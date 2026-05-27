package com.example.classschedule.data

import android.content.Context
import com.example.classschedule.data.course.CourseDatabase
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.course.CourseTableRepository
import com.example.classschedule.data.course.OfflineCourseRepository
import com.example.classschedule.data.course.OfflineCourseTableRepository
import com.example.classschedule.data.schedule.OfflineTimeTableRepository
import com.example.classschedule.data.schedule.TimeTableRepository
import com.example.classschedule.data.schedule.OfflineScheduleRepository
import com.example.classschedule.data.schedule.ScheduleDatabase
import com.example.classschedule.data.schedule.ScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


interface AppContainer {
    val courseRepository: CourseRepository
    val scheduleRepository: ScheduleRepository
    val courseTableRepository: CourseTableRepository
    val timeTableRepository: TimeTableRepository
    val applicationScope: CoroutineScope
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val applicationScope: CoroutineScope = CoroutineScope(SupervisorJob())

    override val courseRepository: CourseRepository by lazy {
        OfflineCourseRepository(CourseDatabase.getDatabase(context).courseDao())
    }

    override val courseTableRepository: CourseTableRepository by lazy {
        OfflineCourseTableRepository(CourseDatabase.getDatabase(context).courseTableDao())
    }

    override val scheduleRepository: ScheduleRepository =
        OfflineScheduleRepository(
            ScheduleDatabase.getDatabase(context).scheduleDao(),
            appScope = applicationScope
        )

    override val timeTableRepository: TimeTableRepository by lazy {
        OfflineTimeTableRepository(ScheduleDatabase.getDatabase(context).timeTableDao())
    }
}