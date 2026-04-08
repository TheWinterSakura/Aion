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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * 为 Composable 内容添加预测性返回手势预览效果。
 * 侧滑时当前页面会缩小并向手势方向偏移，松手后触发 onBack。
 *
 * @param enabled 是否启用（通常在有上级页面时才启用）
 * @param onBack 确认返回时的回调
 * @param content 页面内容
 */
@Composable
fun PredictiveBackGestureHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    val scale = remember { Animatable(1f) }
    val translationX = remember { Animatable(0f) }
    val translationY = remember { Animatable(0f) }
    val cornerRadiusDp = remember { Animatable(0f) }
    var overlayAlpha by remember { mutableFloatStateOf(0f) }

    PredictiveBackHandler(enabled = enabled) { backEvents: Flow<BackEventCompat> ->
        try {
            backEvents.collect { backEvent ->
                val progress = backEvent.progress
                val targetScale = 1f - (0.12f * progress)
                val targetTranslationX = when (backEvent.swipeEdge) {
                    BackEventCompat.EDGE_LEFT -> 80f * progress
                    BackEventCompat.EDGE_RIGHT -> -80f * progress
                    else -> 0f
                }
                val targetTranslationY = (backEvent.touchY - 900f) * 0.015f * progress
                val targetCorner = 32f * progress
                overlayAlpha = 0.25f * progress

                scope.launch {
                    launch { scale.animateTo(targetScale, spring(stiffness = 700f)) }
                    launch { translationX.animateTo(targetTranslationX, spring(stiffness = 700f)) }
                    launch { translationY.animateTo(targetTranslationY, spring(stiffness = 700f)) }
                    launch { cornerRadiusDp.animateTo(targetCorner, spring(stiffness = 700f)) }
                }
            }
            onBack()
        } catch (e: CancellationException) {
            scope.launch {
                launch { scale.animateTo(1f, spring(stiffness = 350f, dampingRatio = 1f)) }
                launch { translationX.animateTo(0f, spring(stiffness = 350f, dampingRatio = 1f)) }
                launch { translationY.animateTo(0f, spring(stiffness = 350f, dampingRatio = 1f)) }
                launch { cornerRadiusDp.animateTo(0f, spring(stiffness = 350f, dampingRatio = 1f)) }
                overlayAlpha = 0f
            }
        }
    }

    val surface = MaterialTheme.colorScheme.surface
    val overlayColor = if (surface.luminance() > 0.5f) androidx.compose.ui.graphics.Color.Black
                       else androidx.compose.ui.graphics.Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                if (overlayAlpha > 0f) {
                    drawRect(overlayColor.copy(alpha = overlayAlpha))
                }
            }
            .clip(RoundedCornerShape(cornerRadiusDp.value.coerceAtLeast(0f).dp))
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
