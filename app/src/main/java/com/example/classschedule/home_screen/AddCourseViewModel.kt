package com.example.classschedule.home_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.tools.showToast
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddCourseViewModel(
    private val repository: CourseRepository,
    userPreferencesRepository: UserPreferencesRepository
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

    val maxPeriodsPerDay = userPreferencesRepository.courseNumberTotal.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 20,
    )
}