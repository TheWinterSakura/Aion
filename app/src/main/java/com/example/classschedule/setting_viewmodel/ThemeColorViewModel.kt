package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeColorViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val themeColor = repository.themeColor.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "Indigo"
    )

    fun saveThemeColor(name: String) {
        viewModelScope.launch {
            repository.saveThemeColor(name)
        }
    }
}
