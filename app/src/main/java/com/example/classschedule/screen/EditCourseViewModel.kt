package com.example.classschedule.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.Course
import com.example.classschedule.data.CourseRepository
import com.example.classschedule.tools.showToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditCourseViewModel(
    private val repository: CourseRepository
): ViewModel() {

    private val _courseId = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val _course: StateFlow<Course?> = _courseId
        .flatMapLatest { id ->
            if (id != 0) {
                repository.getCourseStream(id)
            } else {
                MutableStateFlow(null)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun loadCourse(id: Int) {
        _courseId.value = id
    }

    fun changeCourse(course: Course, context: Context, navigationUp: () -> Unit){
        viewModelScope.launch {
            try {
                repository.updateCourse(course)
                "修改成功".showToast(context = context)
                navigationUp()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}