package com.example.classschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.classschedule.home_screen.AddCourse
import com.example.classschedule.home_screen.CourseDetail
import com.example.classschedule.home_screen.EditCourse
import com.example.classschedule.home_screen.HomeScreen
import com.example.classschedule.setting_screen.AddCourseByJson
import com.example.classschedule.setting_screen.AppDetail
import com.example.classschedule.setting_screen.DataManager
import com.example.classschedule.setting_screen.EasImport
import com.example.classschedule.setting_screen.EditScheduleScreen
import com.example.classschedule.setting_screen.ExportClassSchedule
import com.example.classschedule.setting_screen.ExportClassTime
import com.example.classschedule.setting_screen.ImportScheduleScreen
import com.example.classschedule.setting_screen.LayoutManager
import com.example.classschedule.setting_screen.ScheduleImportScreen
import com.example.classschedule.setting_screen.SchoolDate
import com.example.classschedule.setting_screen.SettingHome
import com.example.classschedule.setting_screen.CourseTableManagerScreen
import com.example.classschedule.setting_screen.QuickEditCoursesScreen
import com.example.classschedule.setting_screen.QuickEditListScreen
import com.example.classschedule.setting_screen.ThemeColorScreen
import com.example.classschedule.setting_screen.TimeTableManagerScreen
import com.example.classschedule.setting_viewmodel.ThemeColorViewModel
import com.example.classschedule.ui.PredictiveBackGestureHandler
import com.example.classschedule.ui.theme.ClassScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeColorViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val themeColor by themeViewModel.themeColor.collectAsState()
            ClassScheduleTheme(themeColorName = themeColor) {
                MainNavScreen(
                    finishAffinity = { finishAffinity() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavScreen(finishAffinity: () -> Unit) {
    val navController = rememberNavController()

    val enterSpring = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
    val exitSpring = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    NavHost(
        navController = navController,
        startDestination = HomeScreen,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = enterSpring
            )
        },
        exitTransition = {
            if (targetState.destination.hasRoute(AddCourseScreen::class) ||
                targetState.destination.hasRoute(EditCourseScreen::class)) {
                scaleOut(targetScale = 0.97f, animationSpec = tween(350)) +
                fadeOut(tween(350), targetAlpha = 0.6f)
            } else {
                scaleOut(targetScale = 0.95f, animationSpec = tween(300)) +
                fadeOut(tween(300), targetAlpha = 0.5f)
            }
        },
        popEnterTransition = {
            if (initialState.destination.hasRoute(AddCourseScreen::class) ||
                initialState.destination.hasRoute(EditCourseScreen::class)) {
                scaleIn(initialScale = 0.97f, animationSpec = tween(350)) +
                fadeIn(tween(350))
            } else {
                scaleIn(initialScale = 0.95f, animationSpec = tween(280)) +
                fadeIn(tween(280))
            }
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = exitSpring
            )
        }
    ) {
        composable<HomeScreen> {
            HomeScreen(
                navigateToAddCourse = {
                    navController.navigate(AddCourseScreen)
                },
                navigateToCourseDetails = { id, weekDate, dayDate, startDate ->
                    navController.navigate(
                        CourseDetailScreen(
                            id = id,
                            weekDate = weekDate,
                            dayDate = dayDate,
                            startDate = startDate
                        )
                    )
                },
                navigateToSetting = {
                    navController.navigate(SettingHomeScreen)
                }
            )
        }

        composable<AddCourseScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                ))
            },
            exitTransition = {
                fadeOut(tween(300), targetAlpha = 0.8f)
            },
            popEnterTransition = {
                fadeIn(tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                ))
            }
        ) {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                AddCourse(
                    navigationUp = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<CourseDetailScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<CourseDetailScreen>()
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                CourseDetail(
                    courseId = args.id,
                    weekDate = args.weekDate,
                    dayDate = args.dayDate,
                    startDate = args.startDate,
                    navigationUp = {
                        navController.navigateUp()
                    },
                    navigateToEdit = { id ->
                        navController.navigate(EditCourseScreen(id = id))
                    }
                )
            }
        }

        composable<EditCourseScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                ))
            },
            exitTransition = {
                fadeOut(tween(300), targetAlpha = 0.8f)
            },
            popEnterTransition = {
                fadeIn(tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                ))
            }
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<EditCourseScreen>()
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                EditCourse(
                    id = args.id,
                    navigationUp = {
                        navController.navigateUp()
                    })
            }
        }

        composable<WebScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<WebScreen>()
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                ImportScheduleScreen(
                    navigateUp = {
                        navController.navigateUp()
                    },
                    universityUrl = args.universityUrl,
                    navigateToHome = {
                        navController.navigate(HomeScreen) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable<IdentifyScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                ScheduleImportScreen(
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<SettingHomeScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                SettingHome(
                    navigateToSchoolDate = { navController.navigate(SchoolDateScreen) },
                    navigateToClassImportByLoad = { navController.navigate(EasImportScreen) },
                    navigateToDataManager = { navController.navigate(DataManagerScreen) },
                    navigateToClassImportByPDF = { navController.navigate(IdentifyScreen) },
                    navigateToLayoutManager = { navController.navigate(LayoutManagerScreen) },
                    navigateToAppDetail = { navController.navigate(AppDetailScreen) },
                    navigateUp = { navController.navigateUp() },
                    navigateToCourseTimeScreen = { totalCourseNumber ->
                        navController.navigate(CourseTimeScreen(totalCourseNumber))
                    },
                    navigateToExportClassSchedule = { navController.navigate(ExportClassScheduleScreen) },
                    navigateToExportClassScheduleTimeScreen = { navController.navigate(ExportClassTimeScreen) },
                    navigateToJsonScreen = { navController.navigate(AddCourseByJsonScreen) },
                    navigateToThemeColor = { navController.navigate(ThemeColorScreen) },
                    navigateToCourseTableManager = { navController.navigate(CourseTableManagerScreen) },
                    navigateToTimeTableManager = { navController.navigate(TimeTableManagerScreen) },
                    navigateToQuickEditByName = { navController.navigate(QuickEditListScreen("name")) },
                    navigateToQuickEditByTeacher = { navController.navigate(QuickEditListScreen("teacher")) }
                )
            }
        }

        composable<SchoolDateScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                SchoolDate(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<EasImportScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                EasImport(
                    onNavigateToWeb = { universityUrl ->
                        navController.navigate(WebScreen(universityUrl = universityUrl))
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<DataManagerScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                DataManager(
                    navigateUp = {
                        navController.navigateUp()
                    },
                    onNavigateToHomeScreen = {
                        navController.navigate(HomeScreen){
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable<LayoutManagerScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                LayoutManager(
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<AppDetailScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                AppDetail(
                    navigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<CourseTimeScreen> { args ->
            val backStackEntry = args.toRoute<CourseTimeScreen>()
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                EditScheduleScreen(
                    navigateUp = {
                        navController.navigateUp()
                    },
                    totalCourseNumber = backStackEntry.totalCourseNumber
                )
            }
        }

        composable<ExportClassScheduleScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                ExportClassSchedule(
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<ExportClassTimeScreen>{
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                ExportClassTime(
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }

        composable<AddCourseByJsonScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                AddCourseByJson(
                    navigateUp = { navController.navigateUp() },
                    navigateToHome = {
                        navController.navigate(HomeScreen) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable<ThemeColorScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                ThemeColorScreen(navigateUp = { navController.navigateUp() })
            }
        }

        composable<CourseTableManagerScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                CourseTableManagerScreen(
                    navigateUp = { navController.navigateUp() },
                    navigateToHome = {
                        navController.navigate(HomeScreen) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        composable<TimeTableManagerScreen> {
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                val settingVm: com.example.classschedule.setting_viewmodel.SettingHomeViewModel =
                    viewModel(factory = AppViewModelProvider.Factory)
                val total by settingVm.totalCourse.collectAsState()
                TimeTableManagerScreen(
                    navigateUp = { navController.navigateUp() },
                    navigateToEdit = { tableId, tableName, totalNum ->
                        navController.navigate(CourseTimeEditScreen(tableId, tableName, totalNum))
                    },
                    totalCourseNumber = total
                )
            }
        }

        composable<CourseTimeEditScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<CourseTimeEditScreen>()
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                EditScheduleScreen(
                    navigateUp = { navController.navigateUp() },
                    totalCourseNumber = args.totalCourseNumber,
                    timeTableId = args.timeTableId
                )
            }
        }

        composable<QuickEditListScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<QuickEditListScreen>()
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                QuickEditListScreen(
                    mode = args.mode,
                    navigateUp = { navController.navigateUp() },
                    navigateToCourses = { key, mode ->
                        navController.navigate(QuickEditCoursesScreen(key, mode))
                    }
                )
            }
        }

        composable<QuickEditCoursesScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<QuickEditCoursesScreen>()
            PredictiveBackGestureHandler(onBack = { navController.navigateUp() }) {
                QuickEditCoursesScreen(
                    key = args.key,
                    mode = args.mode,
                    navigateUp = { navController.navigateUp() },
                    navigateToEdit = { id -> navController.navigate(EditCourseScreen(id)) }
                )
            }
        }
    }
}