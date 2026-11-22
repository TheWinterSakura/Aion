package com.example.classschedule.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.classschedule.HomeScreen

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
                    homeEntries(
                        sharedTransitionScope = sharedTransitionScope,
                        backStack = backStack,
                        navigateUp = { navigateUp() },
                        editorSlideSpring = editorSlideSpring,
                    )
                    settingEntries(
                        backStack = backStack,
                        navigateUp = { navigateUp() },
                        navigateToHome = { navigateToHome() },
                    )
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
