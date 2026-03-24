package com.example.classschedule.setting_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.UserPreferencesRepository
import com.example.classschedule.school.University
import com.example.classschedule.school.loadUniversitiesFromAssets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EasImportViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val universityList = MutableStateFlow<List<University>>(emptyList())

    val universityUrl = repository.universityUrl.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = ""
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
}