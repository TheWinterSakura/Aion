package com.example.classschedule.home_screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.schedule.ScheduleRepository
import com.example.classschedule.notifications.AlertManagerClassScheduleRepository
import com.example.classschedule.notifications.Notification
import com.example.classschedule.tools.getClassTime
import com.example.classschedule.tools.getDayAfterWeeks
import com.example.classschedule.tools.showToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CourseDetailViewModel(
    private val repository: CourseRepository,
    private val alertManager: AlertManagerClassScheduleRepository,
    private val courseTimeRepository: ScheduleRepository
) : ViewModel() {

    private val _courseId = MutableStateFlow(0)

    val courseTimeList = courseTimeRepository.getAllScheduleFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val _course: StateFlow<Course?> = _courseId
        .filter { id -> id > 0 }
        .flatMapLatest { id ->
            repository.getCourseStream(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun loadCourse(id: Int) {
        _courseId.value = id
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            try {
                repository.deleteCourse(course)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addAlert(
        context: Context,
        course: Course,
        weekDate: String,
        dayDate: String,
        startDate: String
    ){
        viewModelScope.launch {
            if (startDate.isNotBlank()){
                alertManager.scheduleExactNotification(
                    notification = Notification(
                        title = course.courseName,
                        content = course.courseLocation,
                        notificationId = course.id
                    ),
                    startDate = startDate,
                    weekDate = weekDate,
                    dayDate = dayDate,
                    startTime = getClassTime(course.courseTime,courseTimeList.value).substringBefore("-"),
                    context = context
                )
            }else{
                "请重新添加".showToast()
            }
        }
    }

    fun cancelAlert(context: Context, alarmId: Int) {
        viewModelScope.launch {
            alertManager.cancelAlarm(context, alarmId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSchedule(
        context: Context,
        title: String,
        location: String,
        startDate: String,
        weekDate: String,
        dayDate: String,
        courseTime: String
    ){
        val scheduleTime = getDayAfterWeeks(startDateStr = startDate, weeksPassed = weekDate.toLong(), dayOfWeek = dayDate)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val startTime = getClassTime(courseTime, allCourseTime = courseTimeList.value).substringBefore("-")
        if (startTime.split(':')[0].length <= 1 && startTime.split(':')[1].length <= 1){
            "时间格式错误".showToast()
            return
        }
        val dateTimeStr = "$scheduleTime $startTime"
        val localDateTime  = LocalDateTime.parse(dateTimeStr,formatter)
        val triggerTimeMillis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (triggerTimeMillis <= System.currentTimeMillis()) {
            "时间已过期".showToast()
            return
        }
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, triggerTimeMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, triggerTimeMillis)
            .putExtra(CalendarContract.Events.TITLE, "$title $location")
            .putExtra(CalendarContract.Reminders.MINUTES, 15)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch ( e: Exception) {
            e.printStackTrace()
            "未找到系统日历应用".showToast()
        }
    }
}
