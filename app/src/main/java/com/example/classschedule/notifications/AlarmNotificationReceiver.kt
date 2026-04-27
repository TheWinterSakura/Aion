package com.example.classschedule.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.classschedule.R

class AlarmNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationId = intent?.getIntExtra("NOTIFICATION_ID", 0)
        val notificationTittle = intent?.getStringExtra("NOTIFICATION_TITTLE")
        val notificationContent = intent?.getStringExtra("NOTIFICATION_CONTENT")
        val notificationIcons = intent?.getIntExtra("NOTIFICATION_ICONS", R.mipmap.app_icon)
        showNotification(
            context = context, notification = Notification(
                notificationId = notificationId ?: 0,
                title = notificationTittle ?: "未知课程",
                content = notificationContent ?: "未知地点",
                icons = notificationIcons ?: R.mipmap.app_icon
            )
        )
    }


    private fun showNotification(context: Context, notification: Notification) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "alert_manager_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "课程提醒通知",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "用于显示课程提醒的通知"
            }
            notificationManager.createNotificationChannel(channel)
        }
        val notificationId = notification.notificationId
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(notification.icons)
            .setContentTitle(notification.title)
            .setContentText(notification.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

}