package com.example.classschedule.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    val COMMENCEMENT_DATE = stringPreferencesKey("commencement_date")
    val ALL_WEEK = stringPreferencesKey("all_week")
    val UNIVERSITY_URL = stringPreferencesKey("university_url")

    val IS_GRID_LAYOUT = booleanPreferencesKey("is_grid_layout")

    val API_KEY = stringPreferencesKey("api_key")
    val COURSE_NUMBER_TOTAL = intPreferencesKey("course_number_total")


    val commencementDate: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[COMMENCEMENT_DATE] ?: "2026-03-02"
        }

    val allWeek: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[ALL_WEEK] ?: "25"
        }

    val universityUrl: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[UNIVERSITY_URL] ?: ""
        }

    val isGridLayout: Flow<Boolean> = dataStore.data
        .catch {
            if (it is Exception) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[IS_GRID_LAYOUT] ?: false
        }

    val apiKey: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[API_KEY] ?: ""
        }

    val courseNumberTotal: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[COURSE_NUMBER_TOTAL] ?: 20
        }
    suspend fun saveStartDatePreference(commencementDate: String) {
        dataStore.edit { preferences ->
            preferences[this.COMMENCEMENT_DATE] = commencementDate
        }
    }

    suspend fun saveAllWeek(allWeek: String) {
        dataStore.edit { preferences ->
            preferences[this.ALL_WEEK] = allWeek
        }
    }

    suspend fun saveUniversityUrl(universityUrl: String) {
        dataStore.edit { preferences ->
            preferences[this.UNIVERSITY_URL] = universityUrl
        }
    }

    suspend fun saveGridLayout(isGridLayout: Boolean) {
        dataStore.edit { preferences ->
            preferences[this.IS_GRID_LAYOUT] = isGridLayout
        }
    }

    suspend fun saveApiKey(apiKey: String){
        dataStore.edit { preferences ->
            preferences[this.API_KEY] = apiKey
        }
    }

    suspend fun saveCourseNumberTotal(courseTotal: Int){
        dataStore.edit { preferences ->
            preferences[this.COURSE_NUMBER_TOTAL] = courseTotal
        }
    }

    private companion object {
        val startDate = stringPreferencesKey("commencement_date")
        const val TAG = "UserPreferencesRepo"
    }

}