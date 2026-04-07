package com.example.classschedule.setting_screen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.schedule.ScheduleRepository
import com.example.classschedule.tools.ExportClassSchedule
import kotlinx.coroutines.launch

class ExportClassTimeViewModel(
    private val repository: ScheduleRepository
): ViewModel() {

    val courseTimeList = repository.getAllScheduleFlow()

    inline fun <reified T> exportJsonToUri(
        context: Context,
        uri: Uri,
        data: T
    ) {
        viewModelScope.launch {
            ExportClassSchedule.exportJsonToUri(
                context = context,
                uri = uri,
                data = data
            )
        }
    }
}