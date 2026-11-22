package com.example.classschedule.setting_viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.tools.JsonTool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AddCourseByJsonViewModel @Inject constructor(
    private val repository: CourseRepository,
    private val preferencesRepository: UserPreferencesRepository
): ViewModel() {

    val courseList = MutableStateFlow<List<Course>?>(emptyList())

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