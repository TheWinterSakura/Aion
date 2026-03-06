package com.example.classschedule.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
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
            preferences[COMMENCEMENT_DATE] ?: ""
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

    suspend fun saveUniversityUrl(universityUrl: String){
        dataStore.edit { preferences ->
            preferences[this.UNIVERSITY_URL] = universityUrl
        }
    }


    private companion object {
        val startDate = stringPreferencesKey("commencement_date")
        const val TAG = "UserPreferencesRepo"
    }

}