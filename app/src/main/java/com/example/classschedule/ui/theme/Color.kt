package com.example.classschedule.ui.theme

import androidx.compose.ui.graphics.Color

// ── Material3 基础调色板（Indigo 系）──────────────────────────────────────────
// 亮色 primary
val Primary40 = Color(0xFF3F51B5)
val OnPrimary40 = Color(0xFFFFFFFF)
val PrimaryContainer40 = Color(0xFFDDE1FF)
val OnPrimaryContainer40 = Color(0xFF00105C)

// 深色 primary
val Primary80 = Color(0xFFBAC3FF)
val OnPrimary80 = Color(0xFF08218A)
val PrimaryContainer80 = Color(0xFF273796)
val OnPrimaryContainer80 = Color(0xFFDDE1FF)

// 亮色 secondary
val Secondary40 = Color(0xFF5C5F7E)
val OnSecondary40 = Color(0xFFFFFFFF)
val SecondaryContainer40 = Color(0xFFE2E3FF)
val OnSecondaryContainer40 = Color(0xFF181B37)

// 深色 secondary
val Secondary80 = Color(0xFFC5C6EA)
val OnSecondary80 = Color(0xFF2E304D)
val SecondaryContainer80 = Color(0xFF444764)
val OnSecondaryContainer80 = Color(0xFFE2E3FF)

// 亮色 tertiary
val Tertiary40 = Color(0xFF79536A)
val OnTertiary40 = Color(0xFFFFFFFF)
val TertiaryContainer40 = Color(0xFFFFD8EE)
val OnTertiaryContainer40 = Color(0xFF2E1125)

// 深色 tertiary
val Tertiary80 = Color(0xFFEDB8D3)
val OnTertiary80 = Color(0xFF48263A)
val TertiaryContainer80 = Color(0xFF603B52)
val OnTertiaryContainer80 = Color(0xFFFFD8EE)

// 亮色 error
val Error40 = Color(0xFFBA1A1A)
val OnError40 = Color(0xFFFFFFFF)
val ErrorContainer40 = Color(0xFFFFDAD6)
val OnErrorContainer40 = Color(0xFF410002)

// 深色 error
val Error80 = Color(0xFFFFB4AB)
val OnError80 = Color(0xFF690005)
val ErrorContainer80 = Color(0xFF93000A)
val OnErrorContainer80 = Color(0xFFFFDAD6)

// 亮色 neutral
val Background40 = Color(0xFFFBF8FF)
val OnBackground40 = Color(0xFF1A1B21)
val Surface40 = Color(0xFFFBF8FF)
val OnSurface40 = Color(0xFF1A1B21)
val SurfaceVariant40 = Color(0xFFE3E1EC)
val OnSurfaceVariant40 = Color(0xFF46464F)
val Outline40 = Color(0xFF777680)
val OutlineVariant40 = Color(0xFFC7C5D0)

// 深色 neutral
val Background80 = Color(0xFF121318)
val OnBackground80 = Color(0xFFE3E1EC)
val Surface80 = Color(0xFF121318)
val OnSurface80 = Color(0xFFE3E1EC)
val SurfaceVariant80 = Color(0xFF46464F)
val OnSurfaceVariant80 = Color(0xFFC7C5D0)
val Outline80 = Color(0xFF918F9A)
val OutlineVariant80 = Color(0xFF46464F)

// ── 课表格子颜色（适配深色/亮色，通过 Theme 提供）────────────────────────────
// 亮色版（柔和低饱和）
val CourseColor1Light = Color(0xFFD8E4FF)  // 蓝
val CourseColor2Light = Color(0xFFEDD8FF)  // 紫
val CourseColor3Light = Color(0xFFD8FFE8)  // 绿
val CourseColor4Light = Color(0xFFFFEDD8)  // 橙
val CourseColor5Light = Color(0xFFFFD8D8)  // 红
val CourseColor6Light = Color(0xFFD8FFFA)  // 青

// 深色版（稍深，保持可读性）
val CourseColor1Dark = Color(0xFF1E3A6E)
val CourseColor2Dark = Color(0xFF3A1E6E)
val CourseColor3Dark = Color(0xFF1E6E3A)
val CourseColor4Dark = Color(0xFF6E3A1E)
val CourseColor5Dark = Color(0xFF6E1E1E)
val CourseColor6Dark = Color(0xFF1E6E65)

// 课表格子文字颜色
val CourseTextLight = Color(0xFF1A1B21)
val CourseTextDark = Color(0xFFE3E1EC)

// ── 主题色方案定义 ─────────────────────────────────────────────────────────────

data class AppColorScheme(
    val name: String,
    val displayName: String,
    val previewColor: Color,
    // light
    val primary40: Color,
    val onPrimary40: Color,
    val primaryContainer40: Color,
    val onPrimaryContainer40: Color,
    val secondary40: Color,
    val onSecondary40: Color,
    val secondaryContainer40: Color,
    val onSecondaryContainer40: Color,
    // dark
    val primary80: Color,
    val onPrimary80: Color,
    val primaryContainer80: Color,
    val onPrimaryContainer80: Color,
    val secondary80: Color,
    val onSecondary80: Color,
    val secondaryContainer80: Color,
    val onSecondaryContainer80: Color,
)

val ThemeColorSchemes = listOf(
    AppColorScheme(
        name = "Indigo", displayName = "靛蓝",
        previewColor = Color(0xFF3F51B5),
        primary40 = Color(0xFF3F51B5), onPrimary40 = Color(0xFFFFFFFF),
        primaryContainer40 = Color(0xFFDDE1FF), onPrimaryContainer40 = Color(0xFF00105C),
        secondary40 = Color(0xFF5C5F7E), onSecondary40 = Color(0xFFFFFFFF),
        secondaryContainer40 = Color(0xFFE2E3FF), onSecondaryContainer40 = Color(0xFF181B37),
        primary80 = Color(0xFFBAC3FF), onPrimary80 = Color(0xFF08218A),
        primaryContainer80 = Color(0xFF273796), onPrimaryContainer80 = Color(0xFFDDE1FF),
        secondary80 = Color(0xFFC5C6EA), onSecondary80 = Color(0xFF2E304D),
        secondaryContainer80 = Color(0xFF444764), onSecondaryContainer80 = Color(0xFFE2E3FF),
    ),
    AppColorScheme(
        name = "Blue", displayName = "海蓝",
        previewColor = Color(0xFF1565C0),
        primary40 = Color(0xFF1565C0), onPrimary40 = Color(0xFFFFFFFF),
        primaryContainer40 = Color(0xFFD3E4FF), onPrimaryContainer40 = Color(0xFF001C3B),
        secondary40 = Color(0xFF535F70), onSecondary40 = Color(0xFFFFFFFF),
        secondaryContainer40 = Color(0xFFD7E3F7), onSecondaryContainer40 = Color(0xFF101C2B),
        primary80 = Color(0xFFA4C8FF), onPrimary80 = Color(0xFF003060),
        primaryContainer80 = Color(0xFF004A97), onPrimaryContainer80 = Color(0xFFD3E4FF),
        secondary80 = Color(0xFFBBC7DB), onSecondary80 = Color(0xFF253140),
        secondaryContainer80 = Color(0xFF3B4858), onSecondaryContainer80 = Color(0xFFD7E3F7),
    ),
    AppColorScheme(
        name = "Teal", displayName = "青绿",
        previewColor = Color(0xFF00695C),
        primary40 = Color(0xFF00695C), onPrimary40 = Color(0xFFFFFFFF),
        primaryContainer40 = Color(0xFFB2DFDB), onPrimaryContainer40 = Color(0xFF00201C),
        secondary40 = Color(0xFF4A635F), onSecondary40 = Color(0xFFFFFFFF),
        secondaryContainer40 = Color(0xFFCCE8E3), onSecondaryContainer40 = Color(0xFF05201C),
        primary80 = Color(0xFF80CBC4), onPrimary80 = Color(0xFF00372F),
        primaryContainer80 = Color(0xFF004F45), onPrimaryContainer80 = Color(0xFFB2DFDB),
        secondary80 = Color(0xFFB1CCC7), onSecondary80 = Color(0xFF1C3531),
        secondaryContainer80 = Color(0xFF324B47), onSecondaryContainer80 = Color(0xFFCCE8E3),
    ),
    AppColorScheme(
        name = "Green", displayName = "森林绿",
        previewColor = Color(0xFF2E7D32),
        primary40 = Color(0xFF2E7D32), onPrimary40 = Color(0xFFFFFFFF),
        primaryContainer40 = Color(0xFFC8E6C9), onPrimaryContainer40 = Color(0xFF002106),
        secondary40 = Color(0xFF526350), onSecondary40 = Color(0xFFFFFFFF),
        secondaryContainer40 = Color(0xFFD4E8D0), onSecondaryContainer40 = Color(0xFF101F10),
        primary80 = Color(0xFF9CCC65), onPrimary80 = Color(0xFF003A04),
        primaryContainer80 = Color(0xFF1B5E20), onPrimaryContainer80 = Color(0xFFC8E6C9),
        secondary80 = Color(0xFFB8CCB4), onSecondary80 = Color(0xFF243423),
        secondaryContainer80 = Color(0xFF3A4B38), onSecondaryContainer80 = Color(0xFFD4E8D0),
    ),
    AppColorScheme(
        name = "Orange", displayName = "暖橙",
        previewColor = Color(0xFFE65100),
        primary40 = Color(0xFFE65100), onPrimary40 = Color(0xFFFFFFFF),
        primaryContainer40 = Color(0xFFFFDBCC), onPrimaryContainer40 = Color(0xFF380E00),
        secondary40 = Color(0xFF77574A), onSecondary40 = Color(0xFFFFFFFF),
        secondaryContainer40 = Color(0xFFFFDBCC), onSecondaryContainer40 = Color(0xFF2C160C),
        primary80 = Color(0xFFFFB59A), onPrimary80 = Color(0xFF5C1A00),
        primaryContainer80 = Color(0xFF883000), onPrimaryContainer80 = Color(0xFFFFDBCC),
        secondary80 = Color(0xFFE7BEAE), onSecondary80 = Color(0xFF442A1E),
        secondaryContainer80 = Color(0xFF5D3F32), onSecondaryContainer80 = Color(0xFFFFDBCC),
    ),
    AppColorScheme(
        name = "Pink", displayName = "玫瑰粉",
        previewColor = Color(0xFFC2185B),
        primary40 = Color(0xFFC2185B), onPrimary40 = Color(0xFFFFFFFF),
        primaryContainer40 = Color(0xFFFFD9E3), onPrimaryContainer40 = Color(0xFF3E001D),
        secondary40 = Color(0xFF74565F), onSecondary40 = Color(0xFFFFFFFF),
        secondaryContainer40 = Color(0xFFFFD9E3), onSecondaryContainer40 = Color(0xFF2B151C),
        primary80 = Color(0xFFFFB0C8), onPrimary80 = Color(0xFF650031),
        primaryContainer80 = Color(0xFF8E0045), onPrimaryContainer80 = Color(0xFFFFD9E3),
        secondary80 = Color(0xFFE3BDC5), onSecondary80 = Color(0xFF422830),
        secondaryContainer80 = Color(0xFF5A3E46), onSecondaryContainer80 = Color(0xFFFFD9E3),
    ),
    AppColorScheme(
        name = "Purple", displayName = "深紫",
        previewColor = Color(0xFF6A1B9A),
        primary40 = Color(0xFF6A1B9A), onPrimary40 = Color(0xFFFFFFFF),
        primaryContainer40 = Color(0xFFEDD9FF), onPrimaryContainer40 = Color(0xFF280048),
        secondary40 = Color(0xFF655A6F), onSecondary40 = Color(0xFFFFFFFF),
        secondaryContainer40 = Color(0xFFECDCF6), onSecondaryContainer40 = Color(0xFF201829),
        primary80 = Color(0xFFD9AAFF), onPrimary80 = Color(0xFF430070),
        primaryContainer80 = Color(0xFF5A0080), onPrimaryContainer80 = Color(0xFFEDD9FF),
        secondary80 = Color(0xFFD0C0DA), onSecondary80 = Color(0xFF362A3F),
        secondaryContainer80 = Color(0xFF4D4157), onSecondaryContainer80 = Color(0xFFECDCF6),
    ),
    AppColorScheme(
        name = "Red", displayName = "中国红",
        previewColor = Color(0xFFC62828),
        primary40 = Color(0xFFC62828), onPrimary40 = Color(0xFFFFFFFF),
        primaryContainer40 = Color(0xFFFFDAD6), onPrimaryContainer40 = Color(0xFF410002),
        secondary40 = Color(0xFF775651), onSecondary40 = Color(0xFFFFFFFF),
        secondaryContainer40 = Color(0xFFFFDAD6), onSecondaryContainer40 = Color(0xFF2C1512),
        primary80 = Color(0xFFFFB4AB), onPrimary80 = Color(0xFF690005),
        primaryContainer80 = Color(0xFF93000A), onPrimaryContainer80 = Color(0xFFFFDAD6),
        secondary80 = Color(0xFFE7BDB8), onSecondary80 = Color(0xFF442926),
        secondaryContainer80 = Color(0xFF5D3F3C), onSecondaryContainer80 = Color(0xFFFFDAD6),
    ),
)
