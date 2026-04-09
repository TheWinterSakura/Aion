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

// 快捷修改：选择类型（byName / byTeacher）
@Serializable
data class QuickEditListScreen(val mode: String) // mode = "name" | "teacher"

// 快捷修改：展示同名/同教师课程列表
@Serializable
data class QuickEditCoursesScreen(val key: String, val mode: String)

