package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.schedule.TimeTable
import com.example.classschedule.data.schedule.TimeTableRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimeTableManagerViewModel(
    private val timeTableRepository: TimeTableRepository,
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
            timeTableRepository.insert(TimeTable(name = name))
        }
    }

    fun renameTable(table: TimeTable, newName: String) {
        viewModelScope.launch {
            timeTableRepository.update(table.copy(name = newName))
        }
    }

    fun deleteTable(table: TimeTable) {
        viewModelScope.launch {
            timeTableRepository.delete(table)
        }
    }

    fun setActiveTable(id: Int) {
        viewModelScope.launch {
            preferencesRepository.saveActiveTimeTableId(id)
        }
    }
}
