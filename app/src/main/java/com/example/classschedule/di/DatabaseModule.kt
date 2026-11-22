package com.example.classschedule.di

import android.content.Context
import androidx.room.Room
import com.example.classschedule.data.course.CourseDao
import com.example.classschedule.data.course.CourseDatabase
import com.example.classschedule.data.course.CourseTableDao
import com.example.classschedule.data.schedule.ScheduleDao
import com.example.classschedule.data.schedule.ScheduleDatabase
import com.example.classschedule.data.schedule.TimeTableDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCourseDatabase(
        @ApplicationContext context: Context
    ): CourseDatabase =
        Room.databaseBuilder(context, CourseDatabase::class.java, "course_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCourseDao(database: CourseDatabase): CourseDao = database.courseDao()

    @Provides
    fun provideCourseTableDao(database: CourseDatabase): CourseTableDao =
        database.courseTableDao()

    @Provides
    @Singleton
    fun provideScheduleDatabase(
        @ApplicationContext context: Context
    ): ScheduleDatabase =
        Room.databaseBuilder(context, ScheduleDatabase::class.java, "schedule_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideScheduleDao(database: ScheduleDatabase): ScheduleDao = database.scheduleDao()

    @Provides
    fun provideTimeTableDao(database: ScheduleDatabase): TimeTableDao =
        database.timeTableDao()
}
