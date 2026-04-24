package com.example.classschedule.notifications

import com.example.classschedule.R

data class Notification(
    val notificationId: Int = 0,
    val icons: Int = R.mipmap.uchiha_sasuke,
    val title: String,
    val content: String,
)