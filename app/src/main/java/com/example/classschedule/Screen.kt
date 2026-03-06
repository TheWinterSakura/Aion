package com.example.classschedule

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object AddCourseScreen

@Serializable
data class CourseDetailScreen(
    val id: Int
)

@Serializable
data class EditCourseScreen(
    val id: Int
)

@Serializable
data class WebScreen(val universityUrl: String)

@Serializable
object SettingScreen

