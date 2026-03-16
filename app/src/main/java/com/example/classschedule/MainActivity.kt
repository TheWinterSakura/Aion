package com.example.classschedule

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.classschedule.screen.AddCourse
import com.example.classschedule.screen.CourseDetail
import com.example.classschedule.screen.EditCourse
import com.example.classschedule.screen.HomeScreen
import com.example.classschedule.screen.ImportScheduleScreen
import com.example.classschedule.screen.SetScreen
import com.example.classschedule.ui.theme.ClassScheduleTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavScreen(finishAffinity: () -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeScreen,
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
                    navController.navigate(SettingScreen)
                }
            )
        }

        composable<AddCourseScreen> {
            AddCourse(
                navigationUp = {
                    navController.navigateUp()
                }
            )
        }

        composable<CourseDetailScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<CourseDetailScreen>()
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

        composable<EditCourseScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<EditCourseScreen>()
            EditCourse(
                id = args.id,
                navigationUp = {
                    navController.navigateUp()
                })
        }

        composable<WebScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<WebScreen>()
            ImportScheduleScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                universityUrl = args.universityUrl
            )
        }

        composable<SettingScreen> {
            SetScreen(
                onNavigateToWeb = { universityUrl ->
                    navController.navigate(WebScreen(universityUrl = universityUrl))
                }
            )
        }

    }
}