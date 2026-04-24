package com.example.classschedule.widget

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.launch

class AppExitRefreshObserver(private val context: Context) : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            CourseWidget().updateAll(context)
        }
    }
}