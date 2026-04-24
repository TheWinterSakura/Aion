package com.example.classschedule.ui

import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun PredictiveBackGestureHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope        = rememberCoroutineScope()
    val scale        = remember { Animatable(1f) }
    val translationX = remember { Animatable(0f) }
    val translationY = remember { Animatable(0f) }
    val cornerRadius = remember { Animatable(0f) }
    val overlayAlpha = remember { Animatable(0f) }

    val surface = MaterialTheme.colorScheme.surface
    val overlayColor = if (surface.luminance() > 0.5f) Color.Black else Color.White

    PredictiveBackHandler(enabled = enabled) { backEvents: Flow<BackEventCompat> ->
        try {
            backEvents.collect { backEvent ->
                val p = backEvent.progress
                // snapTo 不阻塞，直接跳到目标值，不会堵塞 collect
                scale.snapTo(1f - 0.12f * p)
                translationX.snapTo(
                    when (backEvent.swipeEdge) {
                        BackEventCompat.EDGE_LEFT  ->  80f * p
                        BackEventCompat.EDGE_RIGHT -> -80f * p
                        else -> 0f
                    }
                )
                translationY.snapTo((backEvent.touchY - 900f) * 0.015f * p)
                cornerRadius.snapTo((32f * p).coerceAtLeast(0f))
                overlayAlpha.snapTo(0.25f * p)
            }
            // 手势完成，执行返回
            onBack()
        } catch (e: CancellationException) {
            // 手势取消，用 spring 弹回
            scope.launch {
                launch { scale.animateTo(1f,  spring(stiffness = 400f, dampingRatio = 1f)) }
                launch { translationX.animateTo(0f, spring(stiffness = 400f, dampingRatio = 1f)) }
                launch { translationY.animateTo(0f, spring(stiffness = 400f, dampingRatio = 1f)) }
                launch { cornerRadius.animateTo(0f, spring(stiffness = 400f, dampingRatio = 1f)) }
                launch { overlayAlpha.animateTo(0f, spring(stiffness = 400f, dampingRatio = 1f)) }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                if (overlayAlpha.value > 0f) {
                    drawRect(overlayColor.copy(alpha = overlayAlpha.value))
                }
            }
            .clip(RoundedCornerShape(cornerRadius.value.dp))
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.translationX = translationX.value
                this.translationY = translationY.value
            }
    ) {
        content()
    }
}
