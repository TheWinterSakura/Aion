package com.example.classschedule.data

import android.content.Context


interface AppContainer {
    val courseRepository: CourseRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val courseRepository: CourseRepository by lazy {
        OfflineItemsRepository(CourseDatabase.getDatabase(context).courseDao())
    }
}