package com.example.classschedule

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.classschedule.data.course.CourseTable
import com.example.classschedule.data.course.CourseTableRepository
import com.example.classschedule.data.schedule.TimeTable
import com.example.classschedule.data.schedule.TimeTableRepository
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.di.ApplicationScope
import com.example.classschedule.widget.AppExitRefreshObserver
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class ClassScheduleApplication : Application() {
    @Inject lateinit var courseTableRepository: CourseTableRepository
    @Inject lateinit var timeTableRepository: TimeTableRepository
    @Inject lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    @ApplicationScope
    lateinit var appScope: CoroutineScope

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        PDFBoxResourceLoader.init(applicationContext)
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppExitRefreshObserver(this))
        appScope.launch {
            initDefaultTables()
            _isReady.value = true
        }
    }

    private suspend fun initDefaultTables() {
        // 默认课程表
        val existingCourseTables = courseTableRepository.getAll().first()
        if (existingCourseTables.isEmpty()) {
            val newId = courseTableRepository.insert(CourseTable(name = "默认课程表"))
            userPreferencesRepository.saveActiveCourseTableId(newId.toInt())
        }

        // 默认时间表
        val existingTimeTables = timeTableRepository.getAll().first()
        if (existingTimeTables.isEmpty()) {
            val newId = timeTableRepository.insert(TimeTable(name = "默认时间表"))
            userPreferencesRepository.saveActiveTimeTableId(newId.toInt())
        }
    }
}