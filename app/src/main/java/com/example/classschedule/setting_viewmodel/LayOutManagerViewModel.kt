package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LayOutManagerViewModel(
    private val repository: UserPreferencesRepository,
): ViewModel() {


    val isGridLayout = repository.isGridLayout.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = false
    )

    fun saveIsGridLayout(
        isGridLayout: Boolean
    ) {
        viewModelScope.launch {
            repository.saveGridLayout(isGridLayout = isGridLayout)
        }
    }
}