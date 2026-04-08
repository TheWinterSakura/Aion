package com.example.classschedule.setting_viewmodel

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

    // 导出时使用默认时间表（tableId=1），如需多表支持可后续扩展
    val courseTimeList = repository.getAllScheduleFlow(tableId = 1)

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