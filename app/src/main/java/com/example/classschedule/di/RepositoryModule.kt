package com.example.classschedule.di

import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.course.CourseTableRepository
import com.example.classschedule.data.course.OfflineCourseRepository
import com.example.classschedule.data.course.OfflineCourseTableRepository
import com.example.classschedule.data.schedule.OfflineScheduleRepository
import com.example.classschedule.data.schedule.OfflineTimeTableRepository
import com.example.classschedule.data.schedule.ScheduleRepository
import com.example.classschedule.data.schedule.TimeTableRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCourseRepository(
        impl: OfflineCourseRepository
    ): CourseRepository

    @Binds
    @Singleton
    abstract fun bindCourseTableRepository(
        impl: OfflineCourseTableRepository
    ): CourseTableRepository

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        impl: OfflineScheduleRepository
    ): ScheduleRepository

    @Binds
    @Singleton
    abstract fun bindTimeTableRepository(
        impl: OfflineTimeTableRepository
    ): TimeTableRepository
}
