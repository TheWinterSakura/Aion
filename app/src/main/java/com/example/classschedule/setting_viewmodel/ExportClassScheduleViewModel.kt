package com.example.classschedule.setting_viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.course.CourseTableRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.tools.ExportClassSchedule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExportClassScheduleViewModel(
    private val repository: CourseRepository,
    private val courseTableRepository: CourseTableRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val activeTableId = preferencesRepository.activeCourseTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = -1
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val courseList = activeTableId.flatMapLatest { tableId ->
        if (tableId == -1) flow { emit(null) }
        else flow { emit(repository.getCoursesByTableId(tableId)) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeTableName = activeTableId.flatMapLatest { tableId ->
        flow {
            if (tableId == -1) emit("课程表")
            else emit(courseTableRepository.getById(tableId)?.name ?: "课程表")
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "课程表"
    )

    inline fun <reified T> exportJsonToUri(context: Context, uri: Uri, data: T) {
        viewModelScope.launch {
            ExportClassSchedule.exportJsonToUri(context = context, uri = uri, data = data)
        }
    }
}
