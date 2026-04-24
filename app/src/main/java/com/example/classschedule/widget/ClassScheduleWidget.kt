package com.example.classschedule.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.classschedule.MainActivity
import com.example.classschedule.data.AppDataContainer
import com.example.classschedule.data.course.CourseSimple
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore(name = "settings")

private val WidgetBgColor = ColorProvider(day = Color(0xFFF9FAFB), night = Color(0xFF1E1E1E))
private val ItemBgColor = ColorProvider(day = Color(0xFFFFFFFF), night = Color(0xFF2C2C2C))
private val PrimaryTextColor = ColorProvider(day = Color(0xFF111827), night = Color(0xFFF9FAFB))
private val SecondaryTextColor = ColorProvider(day = Color(0xFF6B7280), night = Color(0xFFA1A1AA))
private val AccentColor = ColorProvider(day = Color(0xFF3B82F6), night = Color(0xFF60A5FA))

class CourseWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContainer = AppDataContainer(context)
        val repository = appContainer.courseRepository
        val preferencesRepository = com.example.classschedule.data.user_preferences.UserPreferencesRepository(
            context.dataStore
        )

        val startDate = preferencesRepository.commencementDate.first()
        val currentWeek = if (startDate.isNotBlank()) {
            val startLocalDate = LocalDate.parse(startDate)
            val currentDate = LocalDate.now()
            val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startLocalDate, currentDate)
            ((daysBetween / 7) + 1).toInt().coerceAtLeast(1)
        } else {
            1
        }

        val activeCourseTableId = preferencesRepository.activeCourseTableId.first()
        
        val dayOfWeekStr = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH))
        val todayCourses = repository.getTodayCourseSimple(
            currentWeekDate = currentWeek, 
            today = dayOfWeekStr,
            tableId = activeCourseTableId
        ).first()

        provideContent {
            val size = LocalSize.current
            val isSmallWidget = size.width < 180.dp

            val action = actionStartActivity<MainActivity>()

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(WidgetBgColor)
                    .cornerRadius(16.dp)
                    .padding(12.dp)
                    .clickable(action)
            ) {
                Column(modifier = GlanceModifier.fillMaxSize()) {
                    HeaderRow(dayOfWeekStr)

                    if (todayCourses.isEmpty()) {
                        EmptyState()
                    } else {
                        if (isSmallWidget) {
                            SmallCourseList(todayCourses)
                        } else {
                            LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                                items(todayCourses) { course ->
                                    Box(modifier = GlanceModifier.clickable(action)) {
                                        CourseWidgetItem(course, isSmallWidget = false)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderRow(dayOfWeekStr: String) {
    Row(
        modifier = GlanceModifier.fillMaxWidth().padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "今日课表",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryTextColor)
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Text(
            text = dayOfWeekStr.take(3),
            style = TextStyle(fontSize = 12.sp, color = SecondaryTextColor)
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = GlanceModifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "今天没有课，好好休息！",
            style = TextStyle(color = SecondaryTextColor, fontSize = 14.sp)
        )
    }
}

@Composable
private fun SmallCourseList(courses: List<CourseSimple>) {
    Column(modifier = GlanceModifier.fillMaxSize()) {
        courses.take(2).forEach { course ->
            CourseWidgetItem(course = course, isSmallWidget = true)
        }
        if (courses.size > 2) {
            Text(
                text = "还有 ${courses.size - 2} 节课...",
                style = TextStyle(fontSize = 10.sp, color = SecondaryTextColor),
                modifier = GlanceModifier.padding(top = 4.dp).fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CourseWidgetItem(course: CourseSimple, isSmallWidget: Boolean) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(ItemBgColor)
            .cornerRadius(8.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = GlanceModifier
                .width(4.dp)
                .height(if (isSmallWidget) 28.dp else 36.dp)
                .background(AccentColor)
                .cornerRadius(2.dp)
        ) {}

        Spacer(modifier = GlanceModifier.width(8.dp))

        Column(modifier = GlanceModifier.defaultWeight()) {
            Text(
                text = course.courseName,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isSmallWidget) 13.sp else 14.sp,
                    color = PrimaryTextColor
                ),
                maxLines = 1
            )
            Spacer(modifier = GlanceModifier.height(2.dp))
            Text(
                text = "${course.courseTime} | ${course.courseLocation}",
                style = TextStyle(
                    fontSize = if (isSmallWidget) 10.sp else 11.sp,
                    color = SecondaryTextColor
                ),
                maxLines = 1
            )
        }
    }
}