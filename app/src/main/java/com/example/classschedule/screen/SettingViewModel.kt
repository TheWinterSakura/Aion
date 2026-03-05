package com.example.classschedule.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.CourseRepository
import com.example.classschedule.data.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repository: UserPreferencesRepository,
    private val repositoryCourse: CourseRepository
): ViewModel() {
    fun saveCommencementDate(commencementDate: String) {
        viewModelScope.launch {
            repository.saveStartDatePreference(commencementDate)
        }
    }

    val startDate = repository.commencementDate.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryCourse.deleteAll()
        }
    }

    fun saveAllWeek(allWeek: String) {
        viewModelScope.launch {
            repository.saveAllWeek(allWeek)
        }
    }

    val allWeek = repository.allWeek.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "1"
    )
}
