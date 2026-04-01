package com.example.classschedule.setting_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.data.schedule.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CourseTimeViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    val scheduleList = scheduleRepository.getAllScheduleFlow()
    val autoCalcEnabled = MutableStateFlow(false)

    fun insertCourseTime(schedule: Schedule) {
        viewModelScope.launch {
            scheduleRepository.insertCourseTime(schedule)
        }
    }

    fun updateAll(scheduleList: List<Schedule>) {
        viewModelScope.launch {
            scheduleRepository.updateAll(scheduleList)
        }
    }

    fun changeAutoCalcEnabled(enabled: Boolean) {
        autoCalcEnabled.value = enabled
    }
}
