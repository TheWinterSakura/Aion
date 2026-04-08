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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
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
import com.example.classschedule.ui.PredictiveBackGestureHandler
import com.example.classschedule.ui.theme.ClassScheduleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClassScheduleTheme {
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
    val slideSpring = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    NavHost(
        navController = navController,
        startDestination = HomeScreen,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = slideSpring
            ) + fadeIn(animationSpec = tween(220))
        },
        exitTransition = {
            if (targetState.destination.hasRoute(AddCourseScreen::class) ||
                targetState.destination.hasRoute(EditCourseScreen::class)) {
                fadeOut(tween(300), targetAlpha = 0.5f)
            } else {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = slideSpring
                ) + fadeOut(animationSpec = tween(220))
            }
        },
        popEnterTransition = {
            if (initialState.destination.hasRoute(AddCourseScreen::class) ||
                initialState.destination.hasRoute(EditCourseScreen::class)) {
                fadeIn(tween(300))
            } else {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = slideSpring
                ) + fadeIn(animationSpec = tween(220))
            }
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = slideSpring
            ) + fadeOut(animationSpec = tween(220))
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
                    navigateToSchoolDate = {
                        navController.navigate(SchoolDateScreen)
                    },
                    navigateToClassImportByLoad = {
                        navController.navigate(EasImportScreen)
                    },
                    navigateToDataManager = {
                        navController.navigate(DataManagerScreen)
                    },
                    navigateToClassImportByPDF = {
                        navController.navigate(IdentifyScreen)
                    },
                    navigateToLayoutManager = {
                        navController.navigate(LayoutManagerScreen)
                    },
                    navigateToAppDetail = {
                        navController.navigate(AppDetailScreen)
                    },
                    navigateUp = {
                        navController.navigateUp()
                    },
                    navigateToCourseTimeScreen = { totalCourseNumber ->
                        navController.navigate(CourseTimeScreen(totalCourseNumber))
                    },
                    navigateToExportClassSchedule = {
                        navController.navigate(ExportClassScheduleScreen)
                    },
                    navigateToExportClassScheduleTimeScreen = {
                        navController.navigate(ExportClassTimeScreen)
                    },
                    navigateToJsonScreen = {
                        navController.navigate(AddCourseByJsonScreen)
                    }
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
                    navigateUp = {
                        navController.navigateUp()
                    },
                    navigateToHome = {
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
    }
}