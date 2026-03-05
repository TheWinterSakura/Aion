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
object WebScreen

