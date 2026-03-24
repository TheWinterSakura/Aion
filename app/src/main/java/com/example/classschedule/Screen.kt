package com.example.classschedule

import kotlinx.serialization.Serializable

@Serializable
object HomeScreen

@Serializable
object AddCourseScreen

@Serializable
data class CourseDetailScreen(
    val id: Int,
    val weekDate: String,
    val dayDate: String,
    val startDate: String,
)

@Serializable
data class EditCourseScreen(
    val id: Int
)

@Serializable
data class WebScreen(val universityUrl: String)

@Serializable
object SettingScreen

@Serializable
object IdentifyScreen

@Serializable
object SettingHomeScreen

@Serializable
object SchoolDateScreen

@Serializable
object EasImportScreen

@Serializable
object DataManagerScreen

@Serializable
object LayoutManagerScreen

