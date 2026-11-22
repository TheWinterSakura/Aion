package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
@HiltViewModel
class SettingHomeViewModel @Inject constructor(
    private val preferenceRepository: UserPreferencesRepository,
): ViewModel() {

    val totalCourse = preferenceRepository.courseNumberTotal.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = 2
    )

}