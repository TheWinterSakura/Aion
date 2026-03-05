package com.example.classschedule

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import com.example.classschedule.data.AppContainer
import com.example.classschedule.data.AppDataContainer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.classschedule.data.UserPreferencesRepository


private const val START_DATE_NAME = "start_date"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = START_DATE_NAME
)

class ClassScheduleApplication : Application() {

    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}