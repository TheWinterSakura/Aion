package com.example.classschedule.setting_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SchoolDateViewModel(
    private val repository: UserPreferencesRepository
): ViewModel() {

    val startDate = repository.commencementDate.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = ""
    )

    val allWeek = repository.allWeek.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = "1"
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

}