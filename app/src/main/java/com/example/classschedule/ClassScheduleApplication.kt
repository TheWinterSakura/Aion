package com.example.classschedule

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import com.example.classschedule.data.AppContainer
import com.example.classschedule.data.AppDataContainer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.classschedule.data.UserPreferencesRepository
import com.example.classschedule.notifications.AlertManagerClassScheduleRepository
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader


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

    override fun onCreate() {
        super.onCreate()
        context = this
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        workManagerClassScheduleRepository = AlertManagerClassScheduleRepository(this)
        PDFBoxResourceLoader.init(applicationContext)
    }
}