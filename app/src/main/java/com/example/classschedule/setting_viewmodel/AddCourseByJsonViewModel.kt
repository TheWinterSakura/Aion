package com.example.classschedule.setting_viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.tools.JsonTool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddCourseByJsonViewModel(
    private val repository: CourseRepository,
    private val preferencesRepository: UserPreferencesRepository
): ViewModel() {

    val courseList = MutableStateFlow<List<Course>?>(emptyList())

    private val activeTableId = preferencesRepository.activeCourseTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 1
    )

    fun importJsonFromUri(context: Context, uri: Uri){
        viewModelScope.launch {
            courseList.value = JsonTool.importJsonFromUri<List<Course>>(
                context = context,
                uri = uri
            )
        }
    }

    fun insertCourseList(courseList: List<Course>){
        viewModelScope.launch {
            val tableId = preferencesRepository.activeCourseTableId.first()
            repository.insertCourseList(
                courseList = courseList.map { it.copy(tableId = tableId) }
            )
        }
    }
}