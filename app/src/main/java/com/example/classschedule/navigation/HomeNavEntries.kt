package com.example.classschedule.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.example.classschedule.AddCourseScreen
import com.example.classschedule.CourseDetailScreen
import com.example.classschedule.EditCourseScreen
import com.example.classschedule.HomeScreen
import com.example.classschedule.SettingHomeScreen
import com.example.classschedule.home_screen.AddCourse
import com.example.classschedule.home_screen.CourseDetail
import com.example.classschedule.home_screen.EditCourse
import com.example.classschedule.home_screen.HomeScreen as HomeScreenContent
import com.example.classschedule.ui.PredictiveBackGestureHandler

/**
 * 首页 & 课程编辑相关目的地。
 */
internal fun EntryProviderScope<NavKey>.homeEntries(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
    navigateUp: () -> Unit,
    editorSlideSpring: FiniteAnimationSpec<IntOffset>,
) {
    entry<HomeScreen> {
        HomeScreenContent(
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
        PredictiveBackGestureHandler(onBack = navigateUp) {
            AddCourse(navigationUp = navigateUp)
        }
    }

    entry<CourseDetailScreen> { key ->
        PredictiveBackGestureHandler(onBack = navigateUp) {
            CourseDetail(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                courseId = key.id,
                weekDate = key.weekDate,
                dayDate = key.dayDate,
                startDate = key.startDate,
                navigationUp = navigateUp,
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
        PredictiveBackGestureHandler(onBack = navigateUp) {
            EditCourse(
                id = key.id,
                navigationUp = navigateUp
            )
        }
    }
}
