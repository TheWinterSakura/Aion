package com.example.classschedule.setting_viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.course.Course
import com.example.classschedule.data.course.CourseRepository
import com.example.classschedule.data.schedule.ScheduleImporter
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
)

sealed interface IdentifyEvent {
    data class ShowMessage(val message: String) : IdentifyEvent
}

@HiltViewModel
class IdentifyViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val userRepository: UserPreferencesRepository,
    private val importRepository: ScheduleImporter,
) : ViewModel() {

    val apiKey = userRepository.apiKey.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses = _courses.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<IdentifyEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun parseScheduleImage(uris: List<Uri>, apiKey: String) {
        if (uris.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, success = false) }
            try {
                _courses.value = importRepository.importFromImages(uris, apiKey)
                _uiState.update { it.copy(isLoading = false, success = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("IdentifyViewModel", "图片解析失败", e)
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "解析失败")
                }
            }
        }
    }

    fun parseScheduleFromPdf(uri: Uri, apiKey: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, success = false) }
            try {
                _courses.value = importRepository.importFromPdf(uri, apiKey)
                _uiState.update { it.copy(isLoading = false, success = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("IdentifyViewModel", "PDF 解析失败", e)
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "PDF读取失败")
                }
            }
        }
    }

    fun addCourse() {
        viewModelScope.launch {
            try {
                val tableId = userRepository.activeCourseTableId.first()
                _courses.value.forEach { course ->
                    courseRepository.insertCourse(course.copy(tableId = tableId))
                }
                _events.send(IdentifyEvent.ShowMessage("添加成功"))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("IdentifyViewModel", "添加课程失败", e)
                _events.send(IdentifyEvent.ShowMessage("添加失败"))
            }
        }
    }

    fun changeApiKey(apiKey: String) {
        viewModelScope.launch {
            userRepository.saveApiKey(apiKey = apiKey)
        }
    }
}
