package com.example.classschedule.setting_screen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.tools.JsonTool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddCourseByJsonViewModel(
    private val repository: CourseRepository
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
            repository.insertCourseList(
                courseList = courseList
            )
        }
    }
}