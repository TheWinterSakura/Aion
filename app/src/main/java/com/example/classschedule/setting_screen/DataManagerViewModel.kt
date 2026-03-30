package com.example.classschedule.setting_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.CourseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataManagerViewModel(
    private val repository: CourseRepository
): ViewModel() {

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}