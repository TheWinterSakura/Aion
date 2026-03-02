package com.example.classschedule

import android.app.Application
import com.example.classschedule.data.AppContainer
import com.example.classschedule.data.AppDataContainer

class ClassScheduleApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}