package com.example.classschedule.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.CourseRepository
import com.example.classschedule.data.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.temporal.ChronoUnit


data class CourseSimpleInput(
    val weekDay: String,
    val currentWeekDate: Int,
)

class HomeViewModel(
    private val repository: CourseRepository,
    private val repositoryPreferences: UserPreferencesRepository
) : ViewModel() {

    private val _week = MutableStateFlow<CourseSimpleInput?>(null)

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
        if (input != null) {
            repository.getAllICourseSimple(currentWeekDate = input.currentWeekDate, weekDay = input.weekDay)
        } else {
            MutableStateFlow(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun loadSimpleCourse(
        currentWeekDate: Int,
        weekDay: String
    ) {
        _week.value = CourseSimpleInput(currentWeekDate = currentWeekDate , weekDay = weekDay)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateCurrentWeek(startDateStr: String): Int {
        val startDate = LocalDate.parse(startDateStr)
        val currentDate = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(startDate, currentDate)
        val currentWeek = (daysBetween / 7) + 1
        return currentWeek.toInt()
    }
}