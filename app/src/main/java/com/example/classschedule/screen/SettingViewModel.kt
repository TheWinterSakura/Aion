package com.example.classschedule.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.CourseRepository
import com.example.classschedule.data.UserPreferencesRepository
import com.example.classschedule.school.University
import com.example.classschedule.school.loadUniversitiesFromAssets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repository: UserPreferencesRepository,
    private val repositoryCourse: CourseRepository
) : ViewModel() {

    val universityList = MutableStateFlow<List<University>>(emptyList())

    fun saveCommencementDate(commencementDate: String) {
        viewModelScope.launch {
            repository.saveStartDatePreference(commencementDate)
        }
    }

    val startDate = repository.commencementDate.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    val universityUrl = repository.universityUrl.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    val isGridLayout = repository.isGridLayout.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryCourse.deleteAll()
        }
    }

    fun saveAllWeek(allWeek: String) {
        viewModelScope.launch {
            repository.saveAllWeek(allWeek)
        }
    }

    val allWeek = repository.allWeek.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = "1"
    )

    fun loadSchool(
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            universityList.value =
                loadUniversitiesFromAssets(context = context, fileName = "table_school.csv")
        }
    }

    fun saveUniversity(
        universityUrl: String
    ) {
        viewModelScope.launch {
            repository.saveUniversityUrl(universityUrl = universityUrl)
        }
    }

    fun saveIsGridLayout(
        isGridLayout: Boolean
    ) {
        viewModelScope.launch {
            repository.saveGridLayout(isGridLayout = isGridLayout)
        }
    }
}
