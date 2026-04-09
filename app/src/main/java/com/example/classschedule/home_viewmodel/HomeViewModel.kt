package com.example.classschedule.home_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.data.schedule.ScheduleRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.tools.getDayAfterWeeks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class HomeViewModel(
    private val repository: CourseRepository,
    private val repositoryPreferences: UserPreferencesRepository,
    private val courseRepository: ScheduleRepository
) : ViewModel() {

    private val _week = MutableStateFlow(1)
    private val _hasLoad = MutableStateFlow(false)
    val hasLoad = _hasLoad.asStateFlow()
    private val _isTimerFinished = MutableStateFlow(false)
    val isTimerFinished = _isTimerFinished.asStateFlow()

    var monDateStr = MutableStateFlow("")

    val isGridLayout = repositoryPreferences.isGridLayout.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val startDate = repositoryPreferences.commencementDate.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    val allWeek = repositoryPreferences.allWeek.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    val totalCourseNumber = repositoryPreferences.courseNumberTotal.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 20
    )

    val activeCourseTableId = repositoryPreferences.activeCourseTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 1
    )

    val activeTimeTableId = repositoryPreferences.activeTimeTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 1
    )

    val week = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )

    // 跟随当前激活的时间表
    @OptIn(ExperimentalCoroutinesApi::class)
    val allCourseTime = activeTimeTableId.flatMapLatest { tableId ->
        courseRepository.getAllScheduleFlow(tableId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // 跟随当前激活的课程表 + 当前周
    @OptIn(ExperimentalCoroutinesApi::class)
    val courseList = combine(_week, activeCourseTableId) { week, tableId -> week to tableId }
        .flatMapLatest { (week, tableId) ->
            if (week != 0) repository.getAllICourseSimple(currentWeekDate = week, tableId = tableId)
            else flowOf(emptyList())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun loadSimpleCourse(currentWeekDate: Int) {
        _week.value = currentWeekDate
    }

    fun calculateCurrentWeek(startDateStr: String): Int {
        val startDate = LocalDate.parse(startDateStr)
        val currentDate = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(startDate, currentDate)
        return ((daysBetween / 7) + 1).toInt()
    }

    fun changeLoad() { _hasLoad.value = !_hasLoad.value }

    fun changeIsFinished() { _isTimerFinished.value = true }

    fun getMonDateStr(startDate: String, weeksPassed: Long) {
        monDateStr.value = getDayAfterWeeks(
            startDateStr = startDate,
            weeksPassed = weeksPassed,
            dayOfWeek = "Monday"
        )
    }

    fun insertCourseTime(schedule: Schedule) {
        viewModelScope.launch {
            courseRepository.insertCourseTime(schedule)
        }
    }

    fun updateCourseColor(id: Int, color: String?) {
        viewModelScope.launch {
            repository.updateCourseColor(id, color)
        }
    }

    val guideHomeShown = repositoryPreferences.guideHomeShown.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true  // 默认 true 避免闪烁，等真实值到来
    )

    val guideGridShown = repositoryPreferences.guideGridShown.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    fun markGuideHomeShown() { viewModelScope.launch { repositoryPreferences.markGuideHomeShown() } }
    fun markGuideGridShown() { viewModelScope.launch { repositoryPreferences.markGuideGridShown() } }
}
