package com.example.classschedule.setting_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.data.schedule.ScheduleRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SchoolDateViewModel(
    private val repository: UserPreferencesRepository,
    private val scheduleRepository: ScheduleRepository
): ViewModel() {

    val startDate = repository.commencementDate.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    val allWeek = repository.allWeek.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "1"
    )

    val totalCourse = repository.courseNumberTotal.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 20
    )

    fun saveCommencementDate(commencementDate: String) {
        viewModelScope.launch {
            repository.saveStartDatePreference(commencementDate)
        }
    }

    fun saveAllWeek(allWeek: String) {
        viewModelScope.launch {
            repository.saveAllWeek(allWeek)
        }
    }

    fun saveTotalCourse(totalCourse: String){
        viewModelScope.launch {
            repository.saveCourseNumberTotal(totalCourse.toInt())
        }
    }

    fun deleteByCourseNumber(courseNumber: Int){
        viewModelScope.launch {
            scheduleRepository.deleteByCourseNumber(courseNumber = courseNumber)
        }
    }

    fun insertCourseTime(schedule: Schedule){
        viewModelScope.launch {
            scheduleRepository.insertCourseTime(schedule)
        }
    }

}