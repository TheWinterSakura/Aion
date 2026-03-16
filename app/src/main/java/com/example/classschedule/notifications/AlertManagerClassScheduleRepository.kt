package com.example.classschedule.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.classschedule.tools.getDayAfterWeeks
import com.example.classschedule.tools.showToast
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class AlertManagerClassScheduleRepository(context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleExactNotification(
        context: Context,
        notification: Notification,
        startDate: String,
        weekDate: String,
        dayDate: String,
        startTime: String
    ) {
        if (startDate.isNotBlank() && weekDate.isNotBlank() && dayDate.isNotBlank() && startTime.isNotBlank()) {
            val scheduleTime = getDayAfterWeeks(startDateStr = startDate, weeksPassed = weekDate.toLong(), dayOfWeek = dayDate)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            if (startTime.split(':')[0].length <= 1 && startTime.split(':')[1].length <= 1){
                "时间格式错误".showToast()
                return
            }
            val dateTimeStr = "$scheduleTime $startTime"
            val localDateTime  = LocalDateTime.parse(dateTimeStr,formatter)
            val triggerTimeMillis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            if (triggerTimeMillis <= System.currentTimeMillis()) {
                "时间已过期".showToast()
                return
            }
            val intent = Intent(context, AlarmNotificationReceiver::class.java).apply {
                putExtra("NOTIFICATION_ID", notification.notificationId)
                putExtra("NOTIFICATION_TITTLE", notification.title)
                putExtra("NOTIFICATION_CONTENT", notification.content)
                putExtra("NOTIFICATION_ICONS", notification.icons)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notification.notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            try {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
//                    System.currentTimeMillis() + 5000,
                    pendingIntent
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            "成功设置闹钟，将于：$dateTimeStr 提醒您".showToast()
        }else{
            "数据获取失败，请重启应用后再试".showToast()
        }
    }

    fun cancelAlarm(context: Context, alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}