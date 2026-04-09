package com.example.classschedule.setting_viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.schedule.ScheduleRepository
import com.example.classschedule.data.schedule.TimeTableRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.tools.ExportClassSchedule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExportClassTimeViewModel(
    private val repository: ScheduleRepository,
    private val timeTableRepository: TimeTableRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val activeTableId = preferencesRepository.activeTimeTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 1
    )

    // 跟随当前激活的时间表 ID
    @OptIn(ExperimentalCoroutinesApi::class)
    val courseTimeList = activeTableId.flatMapLatest { tableId ->
        repository.getAllScheduleFlow(tableId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val activeTableName = MutableStateFlow("时间表")

    fun loadTableName() {
        viewModelScope.launch {
            val table = timeTableRepository.getById(activeTableId.value)
            activeTableName.value = table?.name ?: "时间表"
        }
    }

    inline fun <reified T> exportJsonToUri(context: Context, uri: Uri, data: T) {
        viewModelScope.launch {
            ExportClassSchedule.exportJsonToUri(context = context, uri = uri, data = data)
        }
    }
}
