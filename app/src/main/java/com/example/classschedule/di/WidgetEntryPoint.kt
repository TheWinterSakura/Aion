package com.example.classschedule.di

import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Glance Widget 无法使用 @AndroidEntryPoint，
 * 通过 EntryPoint 从 Application 的 Hilt 容器中获取依赖。
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun courseRepository(): CourseRepository
    fun userPreferencesRepository(): UserPreferencesRepository
}
