package com.example.classschedule.setting_viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.data.schedule.ScheduleRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.tools.JsonTool
import com.example.classschedule.tools.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CourseTimeViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val scheduleList = scheduleRepository.getAllScheduleFlow()
    val autoCalcEnabled = MutableStateFlow(false)
    val newScheduleList = MutableStateFlow<List<Schedule>>(emptyList())

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

    fun analysisSchedule(
        context: Context,
        uri: Uri
    ) {
        viewModelScope.launch {
            val inputScheduleList = JsonTool.importJsonFromUri<List<Schedule>>(
                context = context,
                uri = uri
            )
            if (inputScheduleList == null){
                "解析文件失败".showToast()
            }else{
                newScheduleList.value = inputScheduleList
            }
        }
    }

    fun deleteAll(){
        viewModelScope.launch {
            scheduleRepository.deleteAll()
        }
    }

    fun updateAllTimeCount(
        allCount:Int
    ){
        viewModelScope.launch {
            userPreferencesRepository.saveCourseNumberTotal(courseTotal = allCount)
        }
    }
}