package com.example.classschedule.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── CompositionLocal ──────────────────────────────────────────────────────────

data class CourseColors(val colors: List<Color>, val textColor: Color)

val LocalCourseColors = compositionLocalOf {
    CourseColors(
        colors = listOf(
            CourseColor1Light, CourseColor2Light, CourseColor3Light,
            CourseColor4Light, CourseColor5Light, CourseColor6Light
        ),
        textColor = CourseTextLight
    )
}

// ── Theme ─────────────────────────────────────────────────────────────────────

@Composable
fun ClassScheduleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    themeColorName: String = "Indigo",
    content: @Composable () -> Unit
) {
    val scheme = remember(themeColorName) {
        ThemeColorSchemes.find { it.name == themeColorName } ?: ThemeColorSchemes.first()
    }

    val colorScheme = when {
        dynamicColor && themeColorName == "Dynamic" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme(
            primary = scheme.primary80,
            onPrimary = scheme.onPrimary80,
            primaryContainer = scheme.primaryContainer80,
            onPrimaryContainer = scheme.onPrimaryContainer80,
            secondary = scheme.secondary80,
            onSecondary = scheme.onSecondary80,
            secondaryContainer = scheme.secondaryContainer80,
            onSecondaryContainer = scheme.onSecondaryContainer80,
            tertiary = Tertiary80,
            onTertiary = OnTertiary80,
            tertiaryContainer = TertiaryContainer80,
            onTertiaryContainer = OnTertiaryContainer80,
            error = Error80,
            onError = OnError80,
            errorContainer = ErrorContainer80,
            onErrorContainer = OnErrorContainer80,
            background = Background80,
            onBackground = OnBackground80,
            surface = Surface80,
            onSurface = OnSurface80,
            surfaceVariant = SurfaceVariant80,
            onSurfaceVariant = OnSurfaceVariant80,
            outline = Outline80,
            outlineVariant = OutlineVariant80,
        )
        else -> lightColorScheme(
            primary = scheme.primary40,
            onPrimary = scheme.onPrimary40,
            primaryContainer = scheme.primaryContainer40,
            onPrimaryContainer = scheme.onPrimaryContainer40,
            secondary = scheme.secondary40,
            onSecondary = scheme.onSecondary40,
            secondaryContainer = scheme.secondaryContainer40,
            onSecondaryContainer = scheme.onSecondaryContainer40,
            tertiary = Tertiary40,
            onTertiary = OnTertiary40,
            tertiaryContainer = TertiaryContainer40,
            onTertiaryContainer = OnTertiaryContainer40,
            error = Error40,
            onError = OnError40,
            errorContainer = ErrorContainer40,
            onErrorContainer = OnErrorContainer40,
            background = Background40,
            onBackground = OnBackground40,
            surface = Surface40,
            onSurface = OnSurface40,
            surfaceVariant = SurfaceVariant40,
            onSurfaceVariant = OnSurfaceVariant40,
            outline = Outline40,
            outlineVariant = OutlineVariant40,
        )
    }

    val courseColors = remember(darkTheme) {
        if (darkTheme) CourseColors(
            colors = listOf(CourseColor1Dark, CourseColor2Dark, CourseColor3Dark,
                CourseColor4Dark, CourseColor5Dark, CourseColor6Dark),
            textColor = CourseTextDark
        ) else CourseColors(
            colors = listOf(CourseColor1Light, CourseColor2Light, CourseColor3Light,
                CourseColor4Light, CourseColor5Light, CourseColor6Light),
            textColor = CourseTextLight
        )
    }

    val view = LocalView.current

    CompositionLocalProvider(LocalCourseColors provides courseColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.setBackgroundDrawable(
                android.graphics.drawable.ColorDrawable(colorScheme.surface.toArgb())
            )
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
}
