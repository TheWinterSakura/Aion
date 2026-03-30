package com.example.classschedule.home_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.data.schedule.ScheduleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class HomeViewModel(
    private val repository: CourseRepository,
    private val repositoryPreferences: UserPreferencesRepository,
    private val courseRepository: ScheduleRepository
) : ViewModel() {

    private val _week = MutableStateFlow<Int>(1)
    private val _hasLoad = MutableStateFlow<Boolean>(false)
    val hasLoad = _hasLoad.asStateFlow()
    private val _isTimerFinished = MutableStateFlow<Boolean>(false)
    val isTimerFinished = _isTimerFinished.asStateFlow()

    val allCourseTime = courseRepository.getAllScheduleFlow()


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

    val week = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val courseList = _week.flatMapLatest { input ->
        if (input != 0) {
            repository.getAllICourseSimple(currentWeekDate = input)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun loadSimpleCourse(
        currentWeekDate: Int,
    ) {
        _week.value = currentWeekDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateCurrentWeek(startDateStr: String): Int {
        val startDate = LocalDate.parse(startDateStr)
        val currentDate = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(startDate, currentDate)
        val currentWeek = (daysBetween / 7) + 1
        return currentWeek.toInt()
    }

    fun changeLoad(){
        _hasLoad.value = !_hasLoad.value
    }

    fun changeIsFinished(){
        _isTimerFinished.value = true
    }
}