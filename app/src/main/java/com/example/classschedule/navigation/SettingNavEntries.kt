package com.example.classschedule.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.example.classschedule.AddCourseByJsonScreen
import com.example.classschedule.AppDetailScreen
import com.example.classschedule.CourseTableManagerScreen
import com.example.classschedule.CourseTimeEditScreen
import com.example.classschedule.CourseTimeScreen
import com.example.classschedule.DataManagerScreen
import com.example.classschedule.EasImportScreen
import com.example.classschedule.EditCourseScreen
import com.example.classschedule.ExportClassScheduleScreen
import com.example.classschedule.ExportClassTimeScreen
import com.example.classschedule.IdentifyScreen
import com.example.classschedule.LayoutManagerScreen
import com.example.classschedule.QuickEditCoursesScreen
import com.example.classschedule.QuickEditListScreen
import com.example.classschedule.SchoolDateScreen
import com.example.classschedule.SettingHomeScreen
import com.example.classschedule.ThemeColorScreen
import com.example.classschedule.TimeTableManagerScreen
import com.example.classschedule.WebScreen
import com.example.classschedule.setting_screen.AddCourseByJson
import com.example.classschedule.setting_screen.AppDetail
import com.example.classschedule.setting_screen.CourseTableManagerScreen as CourseTableManagerScreenContent
import com.example.classschedule.setting_screen.DataManager
import com.example.classschedule.setting_screen.EasImport
import com.example.classschedule.setting_screen.EditScheduleScreen
import com.example.classschedule.setting_screen.ExportClassSchedule
import com.example.classschedule.setting_screen.ExportClassTime
import com.example.classschedule.setting_screen.ImportScheduleScreen
import com.example.classschedule.setting_screen.LayoutManager
import com.example.classschedule.setting_screen.QuickEditCoursesScreen as QuickEditCoursesScreenContent
import com.example.classschedule.setting_screen.QuickEditListScreen as QuickEditListScreenContent
import com.example.classschedule.setting_screen.ScheduleImportScreen
import com.example.classschedule.setting_screen.SchoolDate
import com.example.classschedule.setting_screen.SettingHome
import com.example.classschedule.setting_screen.ThemeColorScreen as ThemeColorScreenContent
import com.example.classschedule.setting_screen.TimeTableManagerScreen as TimeTableManagerScreenContent
import com.example.classschedule.setting_viewmodel.SettingHomeViewModel
import com.example.classschedule.ui.PredictiveBackGestureHandler

/**
 * 设置、导入导出及其他二级页面。
 */
internal fun EntryProviderScope<NavKey>.settingEntries(
    backStack: NavBackStack<NavKey>,
    navigateUp: () -> Unit,
    navigateToHome: () -> Unit,
) {
    entry<WebScreen> { key ->
        PredictiveBackGestureHandler(onBack = navigateUp) {
            ImportScheduleScreen(
                navigateUp = navigateUp,
                universityUrl = key.universityUrl,
                navigateToHome = navigateToHome
            )
        }
    }

    entry<IdentifyScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            ScheduleImportScreen(onNavigateUp = navigateUp)
        }
    }

    entry<SettingHomeScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            SettingHome(
                navigateToSchoolDate = { backStack.add(SchoolDateScreen) },
                navigateToClassImportByLoad = { backStack.add(EasImportScreen) },
                navigateToDataManager = { backStack.add(DataManagerScreen) },
                navigateToClassImportByPDF = { backStack.add(IdentifyScreen) },
                navigateToLayoutManager = { backStack.add(LayoutManagerScreen) },
                navigateToAppDetail = { backStack.add(AppDetailScreen) },
                navigateUp = navigateUp,
                navigateToCourseTimeScreen = { totalCourseNumber ->
                    backStack.add(CourseTimeScreen(totalCourseNumber))
                },
                navigateToExportClassSchedule = {
                    backStack.add(ExportClassScheduleScreen)
                },
                navigateToExportClassScheduleTimeScreen = {
                    backStack.add(ExportClassTimeScreen)
                },
                navigateToJsonScreen = { backStack.add(AddCourseByJsonScreen) },
                navigateToThemeColor = { backStack.add(ThemeColorScreen) },
                navigateToCourseTableManager = {
                    backStack.add(CourseTableManagerScreen)
                },
                navigateToTimeTableManager = { backStack.add(TimeTableManagerScreen) },
                navigateToQuickEditByName = { backStack.add(QuickEditListScreen("name")) },
                navigateToQuickEditByTeacher = {
                    backStack.add(QuickEditListScreen("teacher"))
                }
            )
        }
    }

    entry<SchoolDateScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            SchoolDate(onNavigateBack = navigateUp)
        }
    }

    entry<EasImportScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            EasImport(
                onNavigateToWeb = { universityUrl ->
                    backStack.add(WebScreen(universityUrl = universityUrl))
                },
                onNavigateBack = navigateUp
            )
        }
    }

    entry<DataManagerScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            DataManager(
                navigateUp = navigateUp,
                onNavigateToHomeScreen = navigateToHome
            )
        }
    }

    entry<LayoutManagerScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            LayoutManager(onBack = navigateUp)
        }
    }

    entry<AppDetailScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            AppDetail(navigateUp = navigateUp)
        }
    }

    entry<CourseTimeScreen> { key ->
        PredictiveBackGestureHandler(onBack = navigateUp) {
            EditScheduleScreen(
                navigateUp = navigateUp,
                totalCourseNumber = key.totalCourseNumber
            )
        }
    }

    entry<ExportClassScheduleScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            ExportClassSchedule(onNavigateUp = navigateUp)
        }
    }

    entry<ExportClassTimeScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            ExportClassTime(onNavigateUp = navigateUp)
        }
    }

    entry<AddCourseByJsonScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            AddCourseByJson(
                navigateUp = navigateUp,
                navigateToHome = navigateToHome
            )
        }
    }

    entry<ThemeColorScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            ThemeColorScreenContent(navigateUp = navigateUp)
        }
    }

    entry<CourseTableManagerScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            CourseTableManagerScreenContent(
                navigateUp = navigateUp,
                navigateToHome = navigateToHome
            )
        }
    }

    entry<TimeTableManagerScreen> {
        PredictiveBackGestureHandler(onBack = navigateUp) {
            val settingVm: SettingHomeViewModel = hiltViewModel()
            val total by settingVm.totalCourse.collectAsStateWithLifecycle()
            TimeTableManagerScreenContent(
                navigateUp = navigateUp,
                navigateToEdit = { tableId, tableName, totalNum ->
                    backStack.add(CourseTimeEditScreen(tableId, tableName, totalNum))
                },
                totalCourseNumber = total
            )
        }
    }

    entry<CourseTimeEditScreen> { key ->
        PredictiveBackGestureHandler(onBack = navigateUp) {
            EditScheduleScreen(
                navigateUp = navigateUp,
                totalCourseNumber = key.totalCourseNumber,
                timeTableId = key.timeTableId
            )
        }
    }

    entry<QuickEditListScreen> { key ->
        PredictiveBackGestureHandler(onBack = navigateUp) {
            QuickEditListScreenContent(
                mode = key.mode,
                navigateUp = navigateUp,
                navigateToCourses = { coursesKey, mode ->
                    backStack.add(QuickEditCoursesScreen(coursesKey, mode))
                }
            )
        }
    }

    entry<QuickEditCoursesScreen> { key ->
        PredictiveBackGestureHandler(onBack = navigateUp) {
            QuickEditCoursesScreenContent(
                key = key.key,
                mode = key.mode,
                navigateUp = navigateUp,
                navigateToEdit = { id ->
                    backStack.add(EditCourseScreen(id))
                }
            )
        }
    }
}
