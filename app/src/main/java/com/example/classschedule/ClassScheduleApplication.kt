package com.example.classschedule

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import com.example.classschedule.data.AppContainer
import com.example.classschedule.data.AppDataContainer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.classschedule.data.course.CourseTable
import com.example.classschedule.data.schedule.TimeTable
import com.example.classschedule.data.user_preferences.UserPreferencesRepository
import com.example.classschedule.notifications.AlertManagerClassScheduleRepository
import com.example.classschedule.widget.AppExitRefreshObserver
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


private const val START_DATE_NAME = "start_date"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = START_DATE_NAME
)

class ClassScheduleApplication : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var workManagerClassScheduleRepository: AlertManagerClassScheduleRepository

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        context = this
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        workManagerClassScheduleRepository = AlertManagerClassScheduleRepository(this)
        PDFBoxResourceLoader.init(applicationContext)
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppExitRefreshObserver(this))

        appScope.launch { initDefaultTables() }
    }

    /**
     * 首次启动时创建默认课程表和时间表，并设为激活状态。
     * 通过检查表列表是否为空来判断是否首次启动，幂等安全。
     */
    private suspend fun initDefaultTables() {
        // 默认课程表
        val existingCourseTables = container.courseTableRepository.getAll().first()
        if (existingCourseTables.isEmpty()) {
            val newId = container.courseTableRepository.insert(CourseTable(name = "默认课程表"))
            userPreferencesRepository.saveActiveCourseTableId(newId.toInt())
        }

        // 默认时间表
        val existingTimeTables = container.timeTableRepository.getAll().first()
        if (existingTimeTables.isEmpty()) {
            val newId = container.timeTableRepository.insert(TimeTable(name = "默认时间表"))
            userPreferencesRepository.saveActiveTimeTableId(newId.toInt())
        }
    }
}