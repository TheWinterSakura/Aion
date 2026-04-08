package com.example.classschedule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.FormatColorReset
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val presetColors = listOf(
    Color(0xFF3F51B5), Color(0xFF1565C0), Color(0xFF00695C), Color(0xFF2E7D32),
    Color(0xFFE65100), Color(0xFFC2185B), Color(0xFF6A1B9A), Color(0xFFC62828),
    Color(0xFF00838F), Color(0xFF558B2F), Color(0xFFF57F17), Color(0xFF4527A0),
    Color(0xFF37474F), Color(0xFF6D4C41), Color(0xFF0277BD), Color(0xFFAD1457),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerBottomSheet(
    initialColor: String?,
    onDismiss: () -> Unit,
    onColorSelected: (String?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val initColor = remember(initialColor) {
        if (initialColor != null) {
            try { Color(android.graphics.Color.parseColor(initialColor)) }
            catch (e: Exception) { null }
        } else null
    }

    var hue by remember { mutableFloatStateOf(initColor?.let { colorToHue(it) } ?: 210f) }
    var saturation by remember { mutableFloatStateOf(initColor?.let { colorToSaturation(it) } ?: 0.7f) }
    var lightness by remember { mutableFloatStateOf(initColor?.let { colorToLightness(it) } ?: 0.45f) }

    val currentColor = remember(hue, saturation, lightness) { hslToColor(hue, saturation, lightness) }
    val hexText = remember(currentColor) { colorToHex(currentColor) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── 标题 + 预览 ──────────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 大圆形预览色块，带阴影
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(currentColor)
                )
                Column {
                    Text(
                        "自定义颜色",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        hexText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // ── 预设颜色 ─────────────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "预设",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // 固定 8 列，两行
                val rows = presetColors.chunked(8)
                rows.forEach { rowColors ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowColors.forEach { color ->
                            val isSelected = colorToHex(color) == hexText
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .then(
                                        if (isSelected) Modifier.border(
                                            2.5.dp,
                                            MaterialTheme.colorScheme.surface,
                                            CircleShape
                                        ).shadow(2.dp, CircleShape)
                                        else Modifier
                                    )
                                    .clickable {
                                        hue = colorToHue(color)
                                        saturation = colorToSaturation(color)
                                        lightness = colorToLightness(color)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check, null,
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── HSL 滑块 ─────────────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "调整",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HslSlider(
                    label = "色相",
                    value = hue,
                    valueText = "${hue.roundToInt()}°",
                    onValueChange = { hue = it },
                    valueRange = 0f..360f,
                    brush = Brush.horizontalGradient(
                        (0..12).map { hslToColor(it * 30f, 0.8f, 0.5f) }
                    )
                )

                HslSlider(
                    label = "饱和度",
                    value = saturation,
                    valueText = "${(saturation * 100).roundToInt()}%",
                    onValueChange = { saturation = it },
                    valueRange = 0f..1f,
                    brush = Brush.horizontalGradient(
                        listOf(hslToColor(hue, 0f, lightness), hslToColor(hue, 1f, lightness))
                    )
                )

                HslSlider(
                    label = "亮度",
                    value = lightness,
                    valueText = "${(lightness * 100).roundToInt()}%",
                    onValueChange = { lightness = it },
                    valueRange = 0f..1f,
                    brush = Brush.horizontalGradient(
                        listOf(Color.Black, hslToColor(hue, saturation, 0.5f), Color.White)
                    )
                )
            }

            // ── 按钮 ─────────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onColorSelected(null) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Outlined.FormatColorReset, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("跟随主题")
                }
                Button(
                    onClick = { onColorSelected(hexText) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("确认")
                }
            }
        }
    }
}

@Composable
private fun HslSlider(
    label: String,
    value: Float,
    valueText: String,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    brush: Brush
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 标签
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(40.dp)
        )

        // 渐变轨道 + 滑块叠加
        Box(
            modifier = Modifier
                .weight(1f)
                .height(36.dp),
            contentAlignment = Alignment.Center
        ) {
            // 渐变轨道
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(brush)
            )
            // 透明轨道的 Slider 叠在上面
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                )
            )
        }

        // 数值
        Text(
            valueText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .width(40.dp)
                .padding(start = 8.dp)
        )
    }
}

// ── 颜色工具函数 ──────────────────────────────────────────────────────────────

fun colorToHex(color: Color): String {
    val argb = color.toArgb()
    return String.format("#%06X", argb and 0xFFFFFF)
}

fun hslToColor(h: Float, s: Float, l: Float): Color {
    val c = (1f - kotlin.math.abs(2f * l - 1f)) * s
    val x = c * (1f - kotlin.math.abs((h / 60f) % 2f - 1f))
    val m = l - c / 2f
    val (r, g, b) = when {
        h < 60  -> Triple(c, x, 0f)
        h < 120 -> Triple(x, c, 0f)
        h < 180 -> Triple(0f, c, x)
        h < 240 -> Triple(0f, x, c)
        h < 300 -> Triple(x, 0f, c)
        else    -> Triple(c, 0f, x)
    }
    return Color(r + m, g + m, b + m)
}

fun colorToHue(color: Color): Float {
    val r = color.red; val g = color.green; val b = color.blue
    val max = maxOf(r, g, b); val min = minOf(r, g, b); val delta = max - min
    if (delta == 0f) return 0f
    return when (max) {
        r -> (60f * (((g - b) / delta) % 6f) + 360f) % 360f
        g -> 60f * ((b - r) / delta + 2f)
        else -> 60f * ((r - g) / delta + 4f)
    }
}

fun colorToSaturation(color: Color): Float {
    val r = color.red; val g = color.green; val b = color.blue
    val max = maxOf(r, g, b); val min = minOf(r, g, b)
    val l = (max + min) / 2f
    val delta = max - min
    return if (delta == 0f) 0f else (delta / (1f - kotlin.math.abs(2f * l - 1f))).coerceIn(0f, 1f)
}

fun colorToLightness(color: Color): Float {
    val r = color.red; val g = color.green; val b = color.blue
    return (maxOf(r, g, b) + minOf(r, g, b)) / 2f
}
