package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class QuickEditViewModel(
    private val repository: CourseRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // 当前激活的课程表 ID
    private val activeTableId = preferencesRepository.activeCourseTableId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 1
    )

    // 只查当前课程表内的不重复课程名
    @OptIn(ExperimentalCoroutinesApi::class)
    val distinctNames = activeTableId.flatMapLatest { tableId ->
        repository.getDistinctCourseNames(tableId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // 只查当前课程表内的不重复教师名
    @OptIn(ExperimentalCoroutinesApi::class)
    val distinctTeachers = activeTableId.flatMapLatest { tableId ->
        repository.getDistinctTeachers(tableId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _selectedKey = MutableStateFlow("")

    // 按课程名查询，限定当前课程表
    @OptIn(ExperimentalCoroutinesApi::class)
    val coursesByName = _selectedKey.flatMapLatest { key ->
        if (key.isBlank()) flowOf(emptyList())
        else repository.getCoursesByName(key, activeTableId.value)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // 按教师名查询，限定当前课程表
    @OptIn(ExperimentalCoroutinesApi::class)
    val coursesByTeacher = _selectedKey.flatMapLatest { key ->
        if (key.isBlank()) flowOf(emptyList())
        else repository.getCoursesByTeacher(key, activeTableId.value)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun selectKey(key: String) { _selectedKey.value = key }
}
