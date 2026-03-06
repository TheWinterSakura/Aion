package com.example.classschedule

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.classschedule.screen.AddCourseViewModel
import com.example.classschedule.screen.CourseDetailViewModel
import com.example.classschedule.screen.EditCourseViewModel
import com.example.classschedule.screen.HomeViewModel
import com.example.classschedule.screen.SettingViewModel
import com.example.classschedule.screen.SpiderViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            HomeViewModel(
                repository = application.container.courseRepository,
                repositoryPreferences = application.userPreferencesRepository
            )
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            AddCourseViewModel(repository = application.container.courseRepository)
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            CourseDetailViewModel(repository = application.container.courseRepository)
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            EditCourseViewModel(repository = application.container.courseRepository)
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            SpiderViewModel(
                repository = application.container.courseRepository,
            )
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            SettingViewModel(
                repository = application.userPreferencesRepository,
                repositoryCourse = application.container.courseRepository
            )
        }
    }
}