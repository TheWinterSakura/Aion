package com.example.classschedule.setting_screen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.tools.ExportClassSchedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExportClassScheduleViewModel(
    private val repository: CourseRepository
) : ViewModel() {

    val courseList = MutableStateFlow<List<Course>>(emptyList())
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

    fun getAllCourse() {
        viewModelScope.launch {
            courseList.value = repository.getAllCourse()
        }
    }
}