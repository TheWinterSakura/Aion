package com.example.classschedule.home_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddCourseViewModel @Inject constructor(
    private val repository: CourseRepository,
    userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val activeTableId = userPreferencesRepository.activeCourseTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 1
    )

    private val _Success = MutableStateFlow(false)
    val success = _Success.asStateFlow()

    fun addCourse(
        course: Course,
    ){
        viewModelScope.launch {
            try {
                repository.insertCourse(item = course.copy(tableId = activeTableId.value))
                _Success.value = true
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