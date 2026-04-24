package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.course.CourseTable
import com.example.classschedule.data.course.CourseTableRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CourseTableManagerViewModel(
    private val courseTableRepository: CourseTableRepository,
    private val courseRepository: CourseRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val tables = courseTableRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val activeTableId = preferencesRepository.activeCourseTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 1
    )

    fun addTable(name: String) {
        viewModelScope.launch {
            courseTableRepository.insert(CourseTable(name = name))
        }
    }

    fun renameTable(table: CourseTable, newName: String) {
        viewModelScope.launch {
            courseTableRepository.update(table.copy(name = newName))
        }
    }

    fun deleteTable(table: CourseTable) {
        viewModelScope.launch {
            val current = tables.value
            if (current.size <= 1) return@launch
            // 先删该课程表下的所有课程，再删表记录本身
            courseRepository.deleteCoursesByTableId(table.id)
            courseTableRepository.delete(table)
            if (table.id == activeTableId.value) {
                val fallback = current.first { it.id != table.id }
                preferencesRepository.saveActiveCourseTableId(fallback.id)
            }
        }
    }

    fun setActiveTable(id: Int) {
        viewModelScope.launch {
            preferencesRepository.saveActiveCourseTableId(id)
        }
    }
}
