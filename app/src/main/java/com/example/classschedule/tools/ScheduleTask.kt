package com.example.classschedule.tools

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


fun getDayAfterWeeks(startDateStr: String, weeksPassed: Long, dayOfWeek: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val startDate = LocalDate.parse(startDateStr, formatter)
    val day = when (dayOfWeek) {
        "Monday" -> DayOfWeek.MONDAY
        "Tuesday" -> DayOfWeek.TUESDAY
        "Wednesday" -> DayOfWeek.WEDNESDAY
        "Thursday" -> DayOfWeek.THURSDAY
        "Friday" -> DayOfWeek.FRIDAY
        "Saturday" -> DayOfWeek.SATURDAY
        "Sunday" -> DayOfWeek.SUNDAY
        else -> {
            "星期几获取失败,已将日期设置为今日".showToast()
            LocalDate.now()
        }
    }

    val target = startDate.plusWeeks(weeksPassed).with(TemporalAdjusters.previousOrSame(day as DayOfWeek))

    return target.format(formatter)
}