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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExportClassScheduleViewModel(
    private val repository: CourseRepository,
    private val courseTableRepository: CourseTableRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val courseList = MutableStateFlow<List<Course>>(emptyList())

    val activeTableId = preferencesRepository.activeCourseTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 1
    )

    // 当前激活课程表的名字，用于文件名
    val activeTableName = MutableStateFlow("课程表")

    fun loadData() {
        viewModelScope.launch {
            val tableId = activeTableId.value
            // 按当前激活的 tableId 过滤
            courseList.value = repository.getAllCourse().filter { it.tableId == tableId }
            // 读取表名
            val table = courseTableRepository.getById(tableId)
            activeTableName.value = table?.name ?: "课程表"
        }
    }

    inline fun <reified T> exportJsonToUri(context: Context, uri: Uri, data: T) {
        viewModelScope.launch {
            ExportClassSchedule.exportJsonToUri(context = context, uri = uri, data = data)
        }
    }
}
