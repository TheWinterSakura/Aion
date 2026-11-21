package com.example.classschedule

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen : NavKey

@Serializable
object AddCourseScreen : NavKey

@Serializable
data class CourseDetailScreen(
    val id: Int,
    val weekDate: String,
    val dayDate: String,
    val startDate: String,
) : NavKey

@Serializable
data class EditCourseScreen(
    val id: Int
) : NavKey

@Serializable
data class WebScreen(val universityUrl: String) : NavKey

@Serializable
object IdentifyScreen : NavKey

@Serializable
object SettingHomeScreen : NavKey

@Serializable
object SchoolDateScreen : NavKey

@Serializable
object EasImportScreen : NavKey

@Serializable
object DataManagerScreen : NavKey

@Serializable
object LayoutManagerScreen : NavKey

@Serializable
object AppDetailScreen : NavKey

@Serializable
data class CourseTimeScreen(val totalCourseNumber: Int) : NavKey

@Serializable
object ExportClassScheduleScreen : NavKey

@Serializable
object ExportClassTimeScreen : NavKey

@Serializable
object AddCourseByJsonScreen : NavKey

@Serializable
object ThemeColorScreen : NavKey

@Serializable
object CourseTableManagerScreen : NavKey

@Serializable
object TimeTableManagerScreen : NavKey

@Serializable
data class CourseTimeEditScreen(val timeTableId: Int, val timeTableName: String, val totalCourseNumber: Int) : NavKey

// 快捷修改：选择类型（byName / byTeacher）
@Serializable
data class QuickEditListScreen(val mode: String) : NavKey // mode = "name" | "teacher"

// 快捷修改：展示同名/同教师课程列表
@Serializable
data class QuickEditCoursesScreen(val key: String, val mode: String) : NavKey
