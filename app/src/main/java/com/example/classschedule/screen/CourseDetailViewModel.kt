package com.example.classschedule.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.Course
import com.example.classschedule.data.CourseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CourseDetailViewModel(
    private val repository: CourseRepository,
) : ViewModel() {

    private val _courseId = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val _course: StateFlow<Course?> = _courseId
        .filter { id -> id > 0 }
        .flatMapLatest { id ->
            repository.getCourseStream(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun loadCourse(id: Int) {
        _courseId.value = id
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            try {
                repository.deleteCourse(course)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
