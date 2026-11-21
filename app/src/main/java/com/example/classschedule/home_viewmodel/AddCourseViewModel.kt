package com.example.classschedule.home_viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.tools.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCourseViewModel(
    private val repository: CourseRepository,
    userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val activeTableId = userPreferencesRepository.activeCourseTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 1
    )

    fun addCourse(
        course: Course,
        context: Context,
        navigationUp:()-> Unit
    ){
        viewModelScope.launch {
            try {
                repository.insertCourse(item = course.copy(tableId = activeTableId.value))
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val distinctCourseNames = activeTableId.flatMapLatest { tableId ->
        repository.getDistinctCourseNames(tableId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val distinctTeachers = activeTableId.flatMapLatest { tableId ->
        repository.getDistinctTeachers(tableId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}