package com.example.classschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.metadata
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.example.classschedule.home_screen.AddCourse
import com.example.classschedule.home_screen.CourseDetail
import com.example.classschedule.home_screen.EditCourse
import com.example.classschedule.home_screen.HomeScreen
import com.example.classschedule.setting_screen.AddCourseByJson
import com.example.classschedule.setting_screen.AppDetail
import com.example.classschedule.setting_screen.CourseTableManagerScreen
import com.example.classschedule.setting_screen.DataManager
import com.example.classschedule.setting_screen.EasImport
import com.example.classschedule.setting_screen.EditScheduleScreen
import com.example.classschedule.setting_screen.ExportClassSchedule
import com.example.classschedule.setting_screen.ExportClassTime
import com.example.classschedule.setting_screen.ImportScheduleScreen
import com.example.classschedule.setting_screen.LayoutManager
import com.example.classschedule.setting_screen.QuickEditCoursesScreen
import com.example.classschedule.setting_screen.QuickEditListScreen
import com.example.classschedule.setting_screen.ScheduleImportScreen
import com.example.classschedule.setting_screen.SchoolDate
import com.example.classschedule.setting_screen.SettingHome
import com.example.classschedule.setting_screen.ThemeColorScreen
import com.example.classschedule.setting_screen.TimeTableManagerScreen
import com.example.classschedule.setting_viewmodel.SettingHomeViewModel
import com.example.classschedule.setting_viewmodel.ThemeColorViewModel
import com.example.classschedule.ui.PredictiveBackGestureHandler
import com.example.classschedule.ui.theme.ClassScheduleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = LocalContext.current.applicationContext as ClassScheduleApplication
            val isReady by app.isReady.collectAsState()
            val themeViewModel: ThemeColorViewModel = hiltViewModel()
            val themeColor by themeViewModel.themeColor.collectAsState()
            ClassScheduleTheme(themeColorName = themeColor) {
                Box(modifier = Modifier.fillMaxSize()) {

                    AnimatedVisibility(
                        visible = isReady,
                        enter = fadeIn(tween(300)),
                        exit = fadeOut()
                    ) {
                        MainNavScreenNav3(finishAffinity = { finishAffinity() })
                    }

                    AnimatedVisibility(
                        visible = !isReady,
                        enter = fadeIn(),
                        exit = fadeOut(tween(200))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavScreenNav3(finishAffinity: () -> Unit) {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(HomeScreen)

    val enterSpring = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
    val exitSpring = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
    val editorSlideSpring = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    fun navigateUp() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        } else {
            finishAffinity()
        }
    }

    fun navigateToHome() {
        backStack.clear()
        backStack.add(HomeScreen)
    }

    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        val sharedTransitionScope = this

        NavDisplay(
            entries = rememberDecoratedNavEntries(
                backStack = backStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<HomeScreen> {
                        HomeScreen(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            navigateToAddCourse = {
                                backStack.add(AddCourseScreen)
                            },
                            navigateToCourseDetails = { id, weekDate, dayDate, startDate ->
                                backStack.add(
                                    CourseDetailScreen(
                                        id = id,
                                        weekDate = weekDate,
                                        dayDate = dayDate,
                                        startDate = startDate
                                    )
                                )
                            },
                            navigateToSetting = {
                                backStack.add(SettingHomeScreen)
                            }
                        )
                    }

                    entry<AddCourseScreen>(
                        metadata = metadata {
                            put(NavDisplay.TransitionKey) {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                    animationSpec = editorSlideSpring
                                ) togetherWith (
                                    scaleOut(targetScale = 0.97f, animationSpec = tween(350)) +
                                        fadeOut(tween(350), targetAlpha = 0.6f)
                                    )
                            }
                            put(NavDisplay.PopTransitionKey) {
                                (
                                    scaleIn(initialScale = 0.97f, animationSpec = tween(350)) +
                                        fadeIn(tween(350))
                                    ) togetherWith slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = editorSlideSpring
                                )
                            }
                            put(NavDisplay.PredictivePopTransitionKey) {
                                (
                                    scaleIn(initialScale = 0.97f, animationSpec = tween(350)) +
                                        fadeIn(tween(350))
                                    ) togetherWith slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = editorSlideSpring
                                )
                            }
                        }
                    ) {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            AddCourse(
                                navigationUp = { navigateUp() }
                            )
                        }
                    }

                    entry<CourseDetailScreen> { key ->
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            CourseDetail(
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                courseId = key.id,
                                weekDate = key.weekDate,
                                dayDate = key.dayDate,
                                startDate = key.startDate,
                                navigationUp = { navigateUp() },
                                navigateToEdit = { id ->
                                    backStack.add(EditCourseScreen(id = id))
                                }
                            )
                        }
                    }

                    entry<EditCourseScreen>(
                        metadata = metadata {
                            put(NavDisplay.TransitionKey) {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                                    animationSpec = editorSlideSpring
                                ) togetherWith (
                                    scaleOut(targetScale = 0.97f, animationSpec = tween(350)) +
                                        fadeOut(tween(350), targetAlpha = 0.6f)
                                    )
                            }
                            put(NavDisplay.PopTransitionKey) {
                                (
                                    scaleIn(initialScale = 0.97f, animationSpec = tween(350)) +
                                        fadeIn(tween(350))
                                    ) togetherWith slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = editorSlideSpring
                                )
                            }
                            put(NavDisplay.PredictivePopTransitionKey) {
                                (
                                    scaleIn(initialScale = 0.97f, animationSpec = tween(350)) +
                                        fadeIn(tween(350))
                                    ) togetherWith slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                                    animationSpec = editorSlideSpring
                                )
                            }
                        }
                    ) { key ->
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            EditCourse(
                                id = key.id,
                                navigationUp = { navigateUp() }
                            )
                        }
                    }

                    entry<WebScreen> { key ->
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            ImportScheduleScreen(
                                navigateUp = { navigateUp() },
                                universityUrl = key.universityUrl,
                                navigateToHome = { navigateToHome() }
                            )
                        }
                    }

                    entry<IdentifyScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            ScheduleImportScreen(
                                onNavigateUp = { navigateUp() }
                            )
                        }
                    }

                    entry<SettingHomeScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            SettingHome(
                                navigateToSchoolDate = { backStack.add(SchoolDateScreen) },
                                navigateToClassImportByLoad = { backStack.add(EasImportScreen) },
                                navigateToDataManager = { backStack.add(DataManagerScreen) },
                                navigateToClassImportByPDF = { backStack.add(IdentifyScreen) },
                                navigateToLayoutManager = { backStack.add(LayoutManagerScreen) },
                                navigateToAppDetail = { backStack.add(AppDetailScreen) },
                                navigateUp = { navigateUp() },
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
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            SchoolDate(
                                onNavigateBack = { navigateUp() }
                            )
                        }
                    }

                    entry<EasImportScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            EasImport(
                                onNavigateToWeb = { universityUrl ->
                                    backStack.add(WebScreen(universityUrl = universityUrl))
                                },
                                onNavigateBack = { navigateUp() }
                            )
                        }
                    }

                    entry<DataManagerScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            DataManager(
                                navigateUp = { navigateUp() },
                                onNavigateToHomeScreen = { navigateToHome() }
                            )
                        }
                    }

                    entry<LayoutManagerScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            LayoutManager(
                                onBack = { navigateUp() }
                            )
                        }
                    }

                    entry<AppDetailScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            AppDetail(
                                navigateUp = { navigateUp() }
                            )
                        }
                    }

                    entry<CourseTimeScreen> { key ->
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            EditScheduleScreen(
                                navigateUp = { navigateUp() },
                                totalCourseNumber = key.totalCourseNumber
                            )
                        }
                    }

                    entry<ExportClassScheduleScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            ExportClassSchedule(
                                onNavigateUp = { navigateUp() }
                            )
                        }
                    }

                    entry<ExportClassTimeScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            ExportClassTime(
                                onNavigateUp = { navigateUp() }
                            )
                        }
                    }

                    entry<AddCourseByJsonScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            AddCourseByJson(
                                navigateUp = { navigateUp() },
                                navigateToHome = { navigateToHome() }
                            )
                        }
                    }

                    entry<ThemeColorScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            ThemeColorScreen(navigateUp = { navigateUp() })
                        }
                    }

                    entry<CourseTableManagerScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            CourseTableManagerScreen(
                                navigateUp = { navigateUp() },
                                navigateToHome = { navigateToHome() }
                            )
                        }
                    }

                    entry<TimeTableManagerScreen> {
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            val settingVm: SettingHomeViewModel = hiltViewModel()
                            val total by settingVm.totalCourse.collectAsState()
                            TimeTableManagerScreen(
                                navigateUp = { navigateUp() },
                                navigateToEdit = { tableId, tableName, totalNum ->
                                    backStack.add(
                                        CourseTimeEditScreen(
                                            tableId,
                                            tableName,
                                            totalNum
                                        )
                                    )
                                },
                                totalCourseNumber = total
                            )
                        }
                    }

                    entry<CourseTimeEditScreen> { key ->
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            EditScheduleScreen(
                                navigateUp = { navigateUp() },
                                totalCourseNumber = key.totalCourseNumber,
                                timeTableId = key.timeTableId
                            )
                        }
                    }

                    entry<QuickEditListScreen> { key ->
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            QuickEditListScreen(
                                mode = key.mode,
                                navigateUp = { navigateUp() },
                                navigateToCourses = { coursesKey, mode ->
                                    backStack.add(QuickEditCoursesScreen(coursesKey, mode))
                                }
                            )
                        }
                    }

                    entry<QuickEditCoursesScreen> { key ->
                        PredictiveBackGestureHandler(onBack = { navigateUp() }) {
                            QuickEditCoursesScreen(
                                key = key.key,
                                mode = key.mode,
                                navigateUp = { navigateUp() },
                                navigateToEdit = { id ->
                                    backStack.add(EditCourseScreen(id))
                                }
                            )
                        }
                    }
                }
            ),
            modifier = Modifier.fillMaxSize(),
            onBack = { navigateUp() },
            sharedTransitionScope = sharedTransitionScope,
            // 默认前进动画：从右侧滑入，旧页面缩小淡出
            transitionSpec = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = enterSpring
                ) togetherWith (
                    scaleOut(targetScale = 0.95f, animationSpec = tween(300)) +
                        fadeOut(tween(300), targetAlpha = 0.5f)
                    )
            },
            // 默认返回动画：旧页面向右滑出，新页面放大淡入
            popTransitionSpec = {
                (
                    scaleIn(initialScale = 0.95f, animationSpec = tween(280)) +
                        fadeIn(tween(280))
                    ) togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = exitSpring
                )
            },
            predictivePopTransitionSpec = {
                (
                    scaleIn(initialScale = 0.95f, animationSpec = tween(280)) +
                        fadeIn(tween(280))
                    ) togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = exitSpring
                )
            }
        )
    }
}