package com.example.classschedule

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.classschedule.home_screen.AddCourseViewModel
import com.example.classschedule.home_screen.CourseDetailViewModel
import com.example.classschedule.home_screen.EditCourseViewModel
import com.example.classschedule.home_screen.HomeViewModel
import com.example.classschedule.setting_screen.CourseTimeViewModel
import com.example.classschedule.setting_screen.DataManagerViewModel
import com.example.classschedule.setting_screen.EasImportViewModel
import com.example.classschedule.setting_screen.IdentifyViewModel
import com.example.classschedule.setting_screen.LayOutManagerViewModel
import com.example.classschedule.setting_screen.SchoolDateViewModel
import com.example.classschedule.setting_screen.SettingHomeViewModel
import com.example.classschedule.setting_screen.SpiderViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            HomeViewModel(
                repository = application.container.courseRepository,
                repositoryPreferences = application.userPreferencesRepository,
                courseRepository = application.container.scheduleRepository
            )
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            AddCourseViewModel(
                repository = application.container.courseRepository,
                userPreferencesRepository = application.userPreferencesRepository
            )
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            CourseDetailViewModel(
                repository = application.container.courseRepository,
                alertManager = application.workManagerClassScheduleRepository,
                courseTimeRepository = application.container.scheduleRepository
            )
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            EditCourseViewModel(
                repository = application.container.courseRepository,
                userPreferencesRepository = application.userPreferencesRepository
            )
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            SpiderViewModel(
                repository = application.container.courseRepository,
            )
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            LayOutManagerViewModel(
                repository = application.userPreferencesRepository,
            )
        }
        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            IdentifyViewModel(
                repository = application.container.courseRepository,
                userRepository = application.userPreferencesRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            SchoolDateViewModel(
                repository = application.userPreferencesRepository,
                scheduleRepository = application.container.scheduleRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            EasImportViewModel(
                repository = application.userPreferencesRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            DataManagerViewModel(
                repository = application.container.courseRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            CourseTimeViewModel(
                scheduleRepository = application.container.scheduleRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            SettingHomeViewModel(
                preferenceRepository = application.userPreferencesRepository,
            )
        }
    }
}