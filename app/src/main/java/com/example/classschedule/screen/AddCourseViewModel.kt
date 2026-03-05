package com.example.classschedule.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.Course
import com.example.classschedule.data.CourseRepository
import com.example.classschedule.tools.showToast
import kotlinx.coroutines.launch

class AddCourseViewModel(
    private val repository: CourseRepository
): ViewModel() {
    fun addCourse(
        course: Course,
        context: Context,
        navigationUp:()-> Unit
    ){
        viewModelScope.launch {
            try {
                repository.insertCourse(item = course)
                "添加成功".showToast(context = context)
                navigationUp()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}