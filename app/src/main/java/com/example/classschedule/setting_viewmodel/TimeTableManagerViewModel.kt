package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.data.schedule.ScheduleRepository
import com.example.classschedule.data.schedule.TimeTable
import com.example.classschedule.data.schedule.TimeTableRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimeTableManagerViewModel(
    private val timeTableRepository: TimeTableRepository,
    private val scheduleRepository: ScheduleRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val tables = timeTableRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val activeTableId = preferencesRepository.activeTimeTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 1
    )

    fun addTable(name: String) {
        viewModelScope.launch {
            val newId = timeTableRepository.insert(TimeTable(name = name))
            // 读取用户设置的一天课程节数，填充空时间条目
            val total = preferencesRepository.courseNumberTotal.first()
            repeat(total) { i ->
                scheduleRepository.insertCourseTime(
                    Schedule(
                        courseNumber = i + 1,
                        startTime = "00:00",
                        endTime = "00:00",
                        tableId = newId.toInt()
                    )
                )
            }
        }
    }

    fun renameTable(table: TimeTable, newName: String) {
        viewModelScope.launch {
            timeTableRepository.update(table.copy(name = newName))
        }
    }

    fun deleteTable(table: TimeTable) {
        viewModelScope.launch {
            val current = tables.value
            if (current.size <= 1) return@launch
            // 先删该时间表下的所有时间条目，再删表记录本身
            scheduleRepository.deleteAllByTableId(table.id)
            timeTableRepository.delete(table)
            if (table.id == activeTableId.value) {
                val fallback = current.first { it.id != table.id }
                preferencesRepository.saveActiveTimeTableId(fallback.id)
            }
        }
    }

    fun setActiveTable(id: Int) {
        viewModelScope.launch {
            preferencesRepository.saveActiveTimeTableId(id)
        }
    }
}
