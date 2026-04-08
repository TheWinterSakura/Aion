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

@Serializable
object AppDetailScreen

@Serializable
data class CourseTimeScreen(val totalCourseNumber: Int)

@Serializable
object ExportClassScheduleScreen

@Serializable
object ExportClassTimeScreen

@Serializable
object AddCourseByJsonScreen

@Serializable
object ThemeColorScreen

@Serializable
object CourseTableManagerScreen

@Serializable
object TimeTableManagerScreen

@Serializable
data class CourseTimeEditScreen(val timeTableId: Int, val timeTableName: String, val totalCourseNumber: Int)

