package com.example.classschedule

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.classschedule.home_viewmodel.AddCourseViewModel
import com.example.classschedule.home_viewmodel.CourseDetailViewModel
import com.example.classschedule.home_viewmodel.EditCourseViewModel
import com.example.classschedule.home_viewmodel.HomeViewModel
import com.example.classschedule.setting_viewmodel.AddCourseByJsonViewModel
import com.example.classschedule.setting_viewmodel.CourseTimeViewModel
import com.example.classschedule.setting_viewmodel.DataManagerViewModel
import com.example.classschedule.setting_viewmodel.EasImportViewModel
import com.example.classschedule.setting_viewmodel.ExportClassScheduleViewModel
import com.example.classschedule.setting_viewmodel.ExportClassTimeViewModel
import com.example.classschedule.setting_viewmodel.IdentifyViewModel
import com.example.classschedule.setting_viewmodel.LayOutManagerViewModel
import com.example.classschedule.setting_viewmodel.SchoolDateViewModel
import com.example.classschedule.setting_viewmodel.SettingHomeViewModel
import com.example.classschedule.setting_viewmodel.SpiderViewModel
import com.example.classschedule.setting_viewmodel.CourseTableManagerViewModel
import com.example.classschedule.setting_viewmodel.QuickEditViewModel
import com.example.classschedule.setting_viewmodel.TimeTableManagerViewModel
import com.example.classschedule.setting_viewmodel.ThemeColorViewModel

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
                scheduleRepository = application.container.scheduleRepository,
                userPreferencesRepository = application.userPreferencesRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            SettingHomeViewModel(
                preferenceRepository = application.userPreferencesRepository,
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            ExportClassScheduleViewModel(
                repository = application.container.courseRepository,
                courseTableRepository = application.container.courseTableRepository,
                preferencesRepository = application.userPreferencesRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            ExportClassTimeViewModel(
                repository = application.container.scheduleRepository,
                timeTableRepository = application.container.timeTableRepository,
                preferencesRepository = application.userPreferencesRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            AddCourseByJsonViewModel(
                repository = application.container.courseRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            ThemeColorViewModel(repository = application.userPreferencesRepository)
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            CourseTableManagerViewModel(
                courseTableRepository = application.container.courseTableRepository,
                preferencesRepository = application.userPreferencesRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            TimeTableManagerViewModel(
                timeTableRepository = application.container.timeTableRepository,
                preferencesRepository = application.userPreferencesRepository
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as ClassScheduleApplication)
            QuickEditViewModel(
                repository = application.container.courseRepository,
                preferencesRepository = application.userPreferencesRepository
            )
        }
    }
}